/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmacyCompany.controller;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pharmacyCompany.model.Entity;
import pharmacyCompany.model.Purchase;
import pharmacyCompany.model.persist.PurchaseADO;

/**
 *
 * @author Alumne
 */
public class PurchaseController implements ControllerInterface {

    private HttpServletRequest request;
    private HttpServletResponse response;

    public PurchaseController() {

    }

    public PurchaseController(HttpServletRequest request, HttpServletResponse response) {
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
                        //add
                        jsonObject = (JSONObject) jsonParser.parse(JSONData);
                        ArrayList specialRequests = (ArrayList) jsonObject.get("specialRequests");
                        String requests = String.join(";", specialRequests);

                        Purchase purchase = new Purchase(Integer.valueOf(0), Integer.valueOf(jsonObject.get("idUser").toString()), Integer.valueOf(jsonObject.get("idProduct").toString()), (String) jsonObject.get("deliveryDate"),
                                requests, (String) jsonObject.get("specialInstructions"));

                        outPutData = purchaseInsert(purchase);

                        request.getSession().setAttribute("purchaseInsert", purchase);
                        break;

                    case 10100:
                        //modify
                        ArrayList<JSONObject> list = new ArrayList<>();
                        list = (ArrayList<JSONObject>) jsonParser.parse(JSONData);
                        
                        outPutData = modifyPurchase(list);
                        break;

                    case 10200:
                        //delete
                        jsonObject = (JSONObject) jsonParser.parse(JSONData);
                        ArrayList sr = (ArrayList) jsonObject.get("specialRequests");
                        String rs = String.join(";", sr);
                        Purchase dp = new Purchase(Integer.valueOf(jsonObject.get("id").toString()), Integer.valueOf(jsonObject.get("idUser").toString()), Integer.valueOf(jsonObject.get("idProduct").toString()), (String) jsonObject.get("deliveryDate"),
                                rs, (String) jsonObject.get("specialInstructions"));
                        outPutData = deletePurchase(dp);
                        break;

                    case 10300:
                        //list
                        outPutData = listAll();
                        break;

                    default:
                        //Sending to client the error                        
                        outPutData.add(false);
                        List<String> errors = new ArrayList<>();
                        errors.add("There has been an error in the server, try later");
                        outPutData.add(errors);
                        //view.setFormattedDataTosend(outPutData);
                        System.out.println("Action is not correct in PurchaseController, action: " + action);
                        break;
                }
            } catch (ParseException ex) {
                System.out.println("Message: " + ex.getMessage());
            }
        }

        return outPutData;
    }
    
    private synchronized ArrayList<Object> listAll() {
        PurchaseADO helper;
        ArrayList<Object> outPutData = new ArrayList<>();

        try {
            helper = new PurchaseADO();

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

    private synchronized ArrayList<Object> purchaseInsert(Purchase purchase) {
        PurchaseADO helper;
        ArrayList<Object> outPutData = new ArrayList<>();

        try {
            helper = new PurchaseADO();

            int rowsAffected = helper.insert(purchase);

            if (rowsAffected == 0) {
                outPutData.add(false);
                List<String> errors = new ArrayList<>();
                errors.add("Purchase not correctly inserted");
                outPutData.add(errors);
            } else {
                outPutData.add(true);
                outPutData.add(purchase);
            }

        } catch (IOException | ClassNotFoundException ex) {
            outPutData.add(false);
            List<String> errors = new ArrayList<>();
            errors.add("There has been an error in the server, try later");
            outPutData.add(errors);
            System.out.println("Internal error while creating new purchase (purchaseInsert): " + ex);
            Logger.getLogger(PurchaseController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return outPutData;
    }
    
    private ArrayList<Object> modifyPurchase(ArrayList<JSONObject> mp) {
        PurchaseADO helper;
        ArrayList<Object> outPutData = new ArrayList<>();

        System.out.println(mp.toString());
        try {
            helper = new PurchaseADO();
            
            int inst=0;
            for(JSONObject purchase: mp){
                Purchase p = new Purchase(
                        Integer.valueOf(purchase.get("id").toString()),
                        Integer.valueOf(purchase.get("idUser").toString()),
                        Integer.valueOf(purchase.get("idProduct").toString()),
                        (String) purchase.get("deliveryDate"), 
                        (String) purchase.get("specialRequests"),
                        (String) purchase.get("specialInstructions"));
                
                inst = helper.update(p);
            }
            
            if (inst == 0) {
                outPutData.add(false);
                List<String> errors = new ArrayList<>();
                errors.add("Error updating purcahse");
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

    private ArrayList<Object> deletePurchase(Purchase dp) {
        PurchaseADO helper;
        ArrayList<Object> outPutData = new ArrayList<>();

        System.out.println(dp.toString());
        try {
            helper = new PurchaseADO();

            int inst = helper.remove(dp);
            if (inst == 0) {
                outPutData.add(false);
                List<String> errors = new ArrayList<>();
                errors.add("Error deleting purchase");
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
