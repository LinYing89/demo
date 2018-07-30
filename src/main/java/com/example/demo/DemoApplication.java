package com.example.demo;

import com.example.demo.communication.MyServer;
import com.example.demo.data.Config;
import com.example.demo.data.CreateManager;
import com.example.demo.data.MsgManager;
import com.example.demo.repository.ConfigRepository;
import com.example.demo.repository.MsgManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
//        SpringApplication app = new SpringApplication(DemoApplication.class);
//        app.addListeners(new ApplicationEventListener());
//        app.run(args);

		SpringApplication.run(DemoApplication.class, args);

	}

	@Bean
	@Autowired
	public Config createConfig(ConfigRepository configRepository){
        return configRepository.getOne(1L);
	}

	@Bean
    @Autowired
    public MyServer runMyServer(Config config){
	    MyServer.PORT = config.getDevicePort();
	    MyServer myServer = new MyServer();
        try {
            myServer.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myServer;
    }

//    @Bean
//    @Autowired
//    public MsgManager createDbData(MsgManagerRepository msgManagerRepository){
//        MsgManager msgManager = CreateManager.createMsgManager();
//        msgManagerRepository.save(msgManager);
//        return msgManager;
//    }
}
