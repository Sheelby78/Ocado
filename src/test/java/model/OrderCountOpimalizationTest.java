package model;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
public class OrderCountOpimalizationTest {

    static Dane dane;
    static OrderCountOpimalization instance;

    @BeforeClass
    public static void setUpClass() {
        instance = new OrderCountOpimalization();
        dane = new Dane();
    }

    //test every example input data in first variant of alghorithm
    @Test
    public void testNotNullFindOptimumCount() throws IOException {
        for(int i = 0; i < dane.args.length; i++){
            Assert.assertNotNull(instance.findOptimum(dane.args[i],true));
        }
    }
    //test every example input data in second variant of alghorithm
    @Test
    public void testNotNullFindOptimumValue() throws IOException {
        for(int i = 0; i < dane.args.length; i++){
            Assert.assertNotNull(instance.findOptimum(dane.args[i],false));
        }
    }
    //test count of results in first variant of alghorithm
    @Test
    public void testCountFindOptimum() throws IOException {
        for(int i = 0; i < dane.forCount.length; i++){
            int first = instance.findOptimum(dane.forCount[i],true).size();
            int second = dane.outputCount[i];
            Assert.assertEquals(first, second);
        }
    }
    //test count of results in second variant of alghorithm
    @Test
    public void testValueFindOptimum() throws IOException {
        for(int i = 0; i < dane.forValue.length; i++){
            int first = instance.findOptimum(dane.forValue[i],false).size();
            int second = dane.outputValue[i];
            Assert.assertEquals(first, second);
        }
    }
}