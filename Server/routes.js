const express = require("express");
const mongo = require('mongodb');
const { requestBody, validationResult, body, header, param, query } = require('express-validator');
const { response, request } = require("express");
const fs = require('fs');

const MongoClient = mongo.MongoClient;
const uri = "mongodb+srv://rojatkaraditi:AprApr_2606@test.z8ya6.mongodb.net/project10DB?retryWrites=true&w=majority";
var client;
var collection;

var connectToDb = function(req,res,next){
    client = new MongoClient(uri, { useNewUrlParser: true, useUnifiedTopology: true});
    client.connect(err => {
      if(err){
          closeConnection();
          return res.status(400).json({"error":"Could not connect to database: "+err});
      }
      collection = client.db("project10DB").collection("items");
      console.log("connected to database");
    next();
    });
};

var closeConnection = function(){
    client.close();
}

const route = express.Router();
route.use("/images",express.static('productImages'));

route.post('/items',connectToDb,async(request,response)=>{
    try{
        var rawdata = fs.readFileSync('discount.json');
        if(!rawdata){
            return response.status(400).json({"error":"No items file found"});
        }
        var items = JSON.parse(rawdata);
       var result = await collection.insertMany(items.results);
       response.status(200).json({"result":'documents inserted'});
    }
    catch(error){
        return response.status(400).json({"error":error.toString()});
    }
    finally{
        closeConnection();
    }
})

route.get('/items',[
    query('region',"region if specified must be either 'grocery','lifestyle' or 'produce'").optional().trim().isIn(['grocery','lifestyle','produce'])
],connectToDb,async(request,response)=>{
    try{
        var err = validationResult(request);
        if(!err.isEmpty()){
            return response.status(400).json({"error":err});
        }

        var query = {};
        if(request.query.region){
            query.region = request.query.region;
        }

        var result = await collection.find(query).toArray();
        console.length
        if(result.length<=0){
            return response.status(400).json({"error":'no items found for the specified region'});
        }

        for(var i=0;i<result.length;i++){
            var discount = (result[i].price * result[i].discount)/100;
            var newPrice = result[i].price - discount;
            result[i].discountPrice = newPrice.toFixed(2);
        }

        return response.status(200).json({"result":result});
    }
    catch(error){
        return response.status(400).json({"error":error.toString()});
    }
    finally{
        closeConnection();
    }


})

module.exports = route; 