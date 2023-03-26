package model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class OrderCountOpimalization {
    public ArrayList<String> findOptimum(String[] args, boolean typeOfAlgorithm) throws IOException {
        //reading input data
        JSONObject storeConfig = readJsonFile(args[0]);
        List<String> pickers1 = toList(storeConfig.getJSONArray("pickers"));
        LocalTime pickingStartTime = LocalTime.parse(storeConfig.getString("pickingStartTime"));
        LocalTime pickingEndTime = LocalTime.parse(storeConfig.getString("pickingEndTime"));
        List<Picker> pickers = new ArrayList<>(pickers1.size());

        //making a list of pickers
        for (String s : pickers1) {
            Picker picker = new Picker(
                    s,
                    pickingStartTime,
                    pickingEndTime
            );
            pickers.add(picker);
        }

        String content = Files.readString(Paths.get(args[1]));
        JSONArray ordersArray = new JSONArray(content);
        List<Order> orders = new ArrayList<>(ordersArray.length());

        //making a list of orders
        for (int i = 0; i < ordersArray.length(); i++) {
            JSONObject orderObj = ordersArray.getJSONObject(i);
            Duration pickingTime = Duration.parse(orderObj.getString("pickingTime"));
            LocalTime completeBy =  LocalTime.parse(orderObj.getString("completeBy"));
            BigDecimal orderValue = new BigDecimal(orderObj.getString("orderValue"));
            if(orderValue.equals(new BigDecimal("0.00"))){
                orderValue = new BigDecimal("1.00");
            }

            Order order = new Order(
                    orderObj.getString("orderId"),
                    orderValue,
                    pickingTime,
                    completeBy,
                    completeBy.minus(pickingTime),
                    pickingTime.dividedBy(orderValue.intValue())
            );
            orders.add(order);
        }

        //bestDuration is time when the longest time order should start be picking in the worst case
        Order bestOrder = Collections.max(orders, Comparator.comparing(Order::getPickingTime));
        Duration bestDuration = bestOrder.getPickingTime();

        // two methods of sorting the list depending on the type of algorithm
        if (typeOfAlgorithm){
            // this is for largest quantity of orders
            orders.sort(Comparator.comparing(Order::getDependency).thenComparing(Order::getPickingTime));
        } else {
            //this is for the most valuable orders
            orders.sort(Comparator.comparing(Order::getDependency).thenComparing(Order::getPickingTime).thenComparing(Order::getValueDependency));
        }

        //this copy of input data
        List<Picker> pickersCopy = new ArrayList<>(pickers);
        List<Order> orderCopy = new ArrayList<>(orders);
        ArrayList<String> score = new ArrayList<>();

        //call a main algorithm function
        alg( pickers, orders, pickingEndTime, bestDuration, score);

        //this is case when quantity of selected orders is different from quantity of input amount of orders
        if (score.size() != orderCopy.size()) {
            score.clear();
            orders = new ArrayList<>(orderCopy);
            pickers = new ArrayList<>(pickersCopy);

            // select type of sorting method for this special case
            if (typeOfAlgorithm) {
                orders.sort(Comparator.comparing(Order::getPickingTime).thenComparing(Order::getCompleteBy));
            } else {
                orders.sort(Comparator.comparing(Order::getValueDependency).thenComparing(Order::getPickingTime).thenComparing(Order::getCompleteBy));
            }

            //setting pickers beginning pickingStartTime
            for (Picker picker : pickers) {
                picker.setPickingStartTime(pickingStartTime);
            }
            //call a main algorithm
            alg(pickers, orders, pickingEndTime, bestDuration, score);
        }
        return score;
    }


    //function for read JSON file
    private JSONObject readJsonFile(String path) throws IOException {
        String content = Files.readString(Paths.get(path));
        return new JSONObject(content);
    }

    //function to convert JSONArray to list
    private <T> List<T> toList(JSONArray array) {
        List<T> list = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
            list.add((T) array.get(i));
        }
        return list;
    }

    //function with main algorithm
    private void alg(List<Picker> pickers, List<Order> orders, LocalTime pickingEndTime, Duration bestDuration, ArrayList<String> score){
        boolean flag = false;
        int counter = 0;

        //main loop
        while(counter != pickers.size() * orders.size()){
            counter = 0;
            for(int i = 0; i < pickers.size(); i++){
                for(int j = 0; j < orders.size(); j++){
                    Order order = orders.get(j);
                    Picker picker = pickers.get(i);
                    LocalTime pickerTime = picker.getPickingStartTime();
                    //counting a time when order picking should start
                    LocalTime orderStartTime = order.getCompleteBy().minus(order.getPickingTime());
                    //when picker is able to take this order
                    if (pickerTime.isBefore(orderStartTime) || pickerTime.equals(orderStartTime)) {
                        //situation when the shortest order is not always the best, because maybe we can pick longer order and this one can fit to another picker
                        if(pickingEndTime.minus(bestDuration).equals(picker.getPickingStartTime()) || pickingEndTime.minus(bestDuration).isBefore(picker.getPickingStartTime())){
                            if (!pickerTime.plus(order.getPickingTime()).equals(pickingEndTime)) {
                                if(orders.size() > j+1){
                                    if (!order.getPickingTime().equals(orders.get(j + 1).getPickingTime())) {
                                        for (Picker value : pickers) {
                                            if (picker.equals(value)) {
                                                continue;
                                            }
                                            if (value.getPickingStartTime().plus(order.getPickingTime()).equals(pickingEndTime)) {
                                                flag = true;
                                                score.add(value.getId() + " " + order.getOrderId() + " " + value.getPickingStartTime());
                                                value.setPickingStartTime(value.getPickingStartTime().plus(order.getPickingTime()));
                                                orders.remove(order);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //situation when order was already picked by another picker
                        if(flag){
                            flag = false;
                            continue;
                        }
                        score.add(picker.getId() + " " + order.getOrderId() + " " + pickerTime);
                        picker.setPickingStartTime(picker.getPickingStartTime().plus(order.getPickingTime()));
                        orders.remove(order);
                        // if picker's work time is end remove him from pickers list
                        if(picker.getPickingStartTime().isAfter(picker.getPickingEndTime()) || picker.getPickingStartTime().equals(picker.getPickingEndTime())){
                            pickers.remove(picker);
                        }
                        break;
                    }
                    counter++;
                }
            }
        }
    }
}