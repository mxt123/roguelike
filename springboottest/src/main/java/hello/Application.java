package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.Calendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner demo(CustomerRepository repository) {
		return (args) -> {

			Long now = Calendar.getInstance().getTime().getTime();
			
			FileWriter fileWriter = null;
			File file = new File("resources/customer2.json");

			// if file doesn't exists, then create it
			if (!file.exists()) {
			    file.createNewFile();
			}
			
			fileWriter = new FileWriter(file,false);
			
			final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
						
			
			int size = 10000;
			
			fileWriter.write("[");
			for (int i = 1;i <= size; i++) {
				final Customer c = new Customer(java.util.UUID.randomUUID().toString(),java.util.UUID.randomUUID().toString(),i);
				final String json = ow.writeValueAsString(c);
				fileWriter.write(json);
				if (i < size) {
					fileWriter.write(",");
				}
			}
			fileWriter.write("]");
			fileWriter.flush();
			
			
			Long fileWritten = Calendar.getInstance().getTime().getTime();
			
			log.info("Time to write json " + (new Long(fileWritten - now).toString()));
			
			//https://stackoverflow.com/questions/6349421/how-to-use-jackson-to-deserialise-an-array-of-objects
			
			//read json file data to String
			Path path = Paths.get("resources/customer2.json");
			byte[] jsonData = Files.readAllBytes(path);
			
			//create ObjectMapper instance
			ObjectMapper objectMapper = new ObjectMapper();
			
			//convert json string to object
			Customer[] cust1 = objectMapper.readValue(jsonData, Customer[].class);
			
			Long fileRead = Calendar.getInstance().getTime().getTime();
			log.info("Time to read json " + (new Long(fileRead - fileWritten).toString()));
						
			
			for (Customer cust : cust1) {
				repository.save(cust);
			}
			
			
			Long dbLoaded = Calendar.getInstance().getTime().getTime();
			log.info("Time to load json into db " + (new Long(dbLoaded - fileWritten ).toString()));
			//repository.save(cust1);
			
			// save a couple of customers
			repository.save(new Customer("Jack", "Bauer"));
			repository.save(new Customer("Chloe", "O'Brian"));
			repository.save(new Customer("Kim", "Bauer"));
			repository.save(new Customer("David", "Palmer"));
			repository.save(new Customer("Michelle", "Dessler"));
			
			for (Customer cust : cust1) {
				repository.save(cust);
			}
			
			// fetch an individual customer by ID
			Long count = repository.count();
			log.info("This many people:");
			log.info("--------------------------------");
			log.info(count.toString());
			log.info("");

			/*
			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Customer customer : repository.findAll()) {
				log.info(customer.toString());
			}
			log.info("");
*/

			// fetch an individual customer by ID
			Customer customer = repository.findOne(1L);
			log.info("Customer found with findOne(1L):");
			log.info("--------------------------------");
			log.info(customer.toString());
			log.info("");

			// fetch customers by last name
			log.info("Customer found with findByLastName('Bauer'):");
			log.info("--------------------------------------------");
			for (Customer bauer : repository.findByLastName("Bauer")) {
				log.info(bauer.toString());
			}
			log.info("");
			
			// fetch customers by last name
			log.info("--------------------------------------------");
			for (Customer hat : repository.findByLastName("hat")) {
				log.info(hat.toString());
			}
			log.info("");
		};
	}

}
