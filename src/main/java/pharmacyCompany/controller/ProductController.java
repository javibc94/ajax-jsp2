/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmacyCompany.controller;

import pharmacyCompany.model.Entity;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import pharmacyCompany.model.User;
import pharmacyCompany.model.persist.UserADO;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pharmacyCompany.model.Product;
import pharmacyCompany.model.persist.ProductADO;

/**
 *
 * @author Alumne
 */
public class ProductController implements ControllerInterface {

    private HttpServletRequest request;
    private HttpServletResponse response;

    public ProductController() {

    }

    public ProductController(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;

    }

    @Override
    public ArrayList<Object> doAction() {
        int action = Integer.parseInt(request.getParameter("action"));
        ArrayList<Object> outPutData = new ArrayList<>();

        if (request.getParameter("JSONData") != null) {
            try {
                // 1. get received JSON data from request                                
                String JSONData = request.getParameter("JSONData");
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject;

                // 2. Accés to database in order to get data  
                switch (action) {
                    case 10000:
                        jsonObject = (JSONObject) jsonParser.parse(JSONData);
                        outPutData = listAll();
                        break;

                    case 10100:
                        jsonObject = (JSONObject) jsonParser.parse(JSONData);
                        Product p = new Product(0, (String) jsonObject.get("name"), Double.valueOf(jsonObject.get("price").toString()));
                        outPutData = addProduct(p);
                        break;

                    case 10200:
                        ArrayList<JSONObject> list = new ArrayList<>();
                        list = (ArrayList<JSONObject>) jsonParser.parse(JSONData);
                        
                        outPutData = modifyProduct(list);
                        break;

                    case 10300:
                        jsonObject = (JSONObject) jsonParser.parse(JSONData);
                        Product dp = new Product(Integer.valueOf(jsonObject.get("id").toString()), (String) jsonObject.get("name"), Double.valueOf(jsonObject.get("price").toString()));
                        outPutData = deleteProduct(dp);
                        break;

                    case 10400:

                        break;

                    case 10500:

                        break;

                    default:
                        //Sending to client the error                        
                        outPutData.add(false);
                        List<String> errors = new ArrayList<>();
                        errors.add("There has been an error in the server, try later");
                        outPutData.add(errors);
                        //view.setFormattedDataTosend(outPutData);
                        System.out.println("Action is not correct in userController, action: " + action);
                        break;
                }
            } catch (ParseException ex) {
                System.out.println("Message: " + ex.getMessage());
            }
        }

        return outPutData;
    }

    private synchronized ArrayList<Object> listAll() {
        ProductADO helper;
        ArrayList<Object> outPutData = new ArrayList<>();

        try {
            helper = new ProductADO();

            Collection<Entity> listProducts = helper.findAll();
            if (listProducts == null) {
                outPutData.add(false);
                List<String> errors = new ArrayList<>();
                errors.add("Error reading products");
                outPutData.add(errors);
            } else {
                outPutData.add(true);
                outPutData.add(listProducts);
            }

        } catch (IOException | ClassNotFoundException ex) {
            outPutData.add(false);
            List<String> errors = new ArrayList<>();
            errors.add("There has been an error in the server, try later");
            outPutData.add(errors);
            System.out.println("Internal error while creating new user (userInsert): " + ex);
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return outPutData;
    }

    private ArrayList<Object> addProduct(Product p) {
        ProductADO helper;
        ArrayList<Object> outPutData = new ArrayList<>();

        System.out.println(p.toString());
        try {
            helper = new ProductADO();

            int inst = helper.insert(p);
            if (inst == 0) {
                outPutData.add(false);
                List<String> errors = new ArrayList<>();
                errors.add("Error inserting products");
                outPutData.add(errors);
            } else {
                outPutData.add(true);
                outPutData.add(p);
            }

        } catch (IOException | ClassNotFoundException ex) {
            outPutData.add(false);
            List<String> errors = new ArrayList<>();
            errors.add("There has been an error in the server, try later");
            outPutData.add(errors);
            System.out.println("Internal error while creating new user (userInsert): " + ex);
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return outPutData;
    }

    private ArrayList<Object> modifyProduct(ArrayList<JSONObject> mp) {
        ProductADO helper;
        ArrayList<Object> outPutData = new ArrayList<>();

        System.out.println(mp.toString());
        try {
            helper = new ProductADO();
            //FOREACH
            int inst=0;
            for(JSONObject product: mp){
                Product p = new Product(
                        Integer.valueOf(product.get("id").toString()), 
                        (String) product.get("name"), 
                        Double.valueOf(product.get("price").toString()));
                
                inst = helper.update(p);
            }
            
            if (inst == 0) {
                outPutData.add(false);
                List<String> errors = new ArrayList<>();
                errors.add("Error updating products");
                outPutData.add(errors);
            } else {
                outPutData.add(true);
                outPutData.add(mp);
            }

        } catch (IOException | ClassNotFoundException ex) {
            outPutData.add(false);
            List<String> errors = new ArrayList<>();
            errors.add("There has been an error in the server, try later");
            outPutData.add(errors);
            System.out.println("Internal error while creating new user (userInsert): " + ex);
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return outPutData;
    }

    private ArrayList<Object> deleteProduct(Product dp) {
        ProductADO helper;
        ArrayList<Object> outPutData = new ArrayList<>();

        System.out.println(dp.toString());
        try {
            helper = new ProductADO();

            int inst = helper.remove(dp);
            if (inst == 0) {
                outPutData.add(false);
                List<String> errors = new ArrayList<>();
                errors.add("Error deleting products");
                outPutData.add(errors);
            } else {
                outPutData.add(true);
                outPutData.add(dp);
            }

        } catch (IOException | ClassNotFoundException ex) {
            outPutData.add(false);
            List<String> errors = new ArrayList<>();
            errors.add("There has been an error in the server, try later");
            outPutData.add(errors);
            System.out.println("Internal error while creating new user (userInsert): " + ex);
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return outPutData;
    }

}
