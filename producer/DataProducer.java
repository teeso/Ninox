import java.util.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
 
public class DataProducer {
    public static void main(String[] args) {
 
        Properties props = new Properties();
        props.put("metadata.broker.list", "172.254.0.7:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");
 
        ProducerConfig config = new ProducerConfig(props);
 
        Producer<String, String> producer = new Producer<String, String>(config);

        //Open csv file
        String csvFile = "data.csv";
        BufferedReader br = null;
        String line = "";

        System.out.println("##########################################################\n");
        System.out.println("Ninox Kafka Producer exemple");
        System.out.println("Input 1 row of data each 10sec on Topic \"incomingData\".");
        System.out.println("##########################################################\n");

	try{

		br = new BufferedReader(new FileReader(csvFile));

        int i = 1;
		while ((line = br.readLine()) != null) {

		    KeyedMessage<String, String> data = new KeyedMessage<String, String>("incomingData", line);
		    producer.send(data);
		
			try {

                System.out.println("Ligne n° "+i+" envoyé.");
                System.out.println(line);
                
				Thread.sleep(10000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}

            i = i+1;
		}
	
	} catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        
        producer.close();
    }
}