package DI.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageServiceImpl implements MessageService{
	
	@Value("#{msgProperties.message}")
	private String message;
	
	/*public void setMessage(String message){
		this.message = message;
	}*/
	
	public String getMessage(){
		return message;
	}

}
