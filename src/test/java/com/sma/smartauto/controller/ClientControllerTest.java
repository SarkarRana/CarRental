package com.sma.smartauto.controller;


import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sma.smartauto.dao.ClientDaoService;
import com.sma.smartauto.domain.Client;
import com.sma.smartauto.domain.ClientType;
import com.sma.smartauto.model.ClientDto;
import com.sma.smartauto.utils.Constants;

//@RunWith(SpringRunner.class)
//@WebMvcTest(value = ClientController.class, secure = false)
public class ClientControllerTest {
/*
	@Value("${app.port}")
	private int port;

	@Value("${app.context-path}")
	private String contextPath;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ClientDaoService clientDaoService;
	final Client mockClient = new Client();
	final ClientDto mockClientDto = new ClientDto();		
	
	@Before
	public void setup(){
		mockClient.setId(1);
		mockClient.setName("FCR");
		ClientType type = new ClientType();
		type.setId(1);
		type.setClientTypeDesc("KEY");
		mockClient.setType(type);
		
		mockClientDto.setClientId(1);
		mockClientDto.setName("FCR");
		mockClientDto.setClientTypeId(1);
		mockClientDto.setClientType("KEY");
		
		
	}
	
	@Test
	public void getClientById() throws Exception {		
		
		Mockito.when(
				clientDaoService.findClientById(Mockito.anyInt())).thenReturn(mockClient);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(createURLWithPort("/clients/1"))
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + Constants.TEST_TOKEN);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse());
		String expected = "{id:1,name:FCR,type:{id:1,clientTypeDesc:KEY}}";

		// {"id":"Course1","name":"Spring","description":"10 Steps, 25 Examples and 10K Students","steps":["Learn Maven","Import Project","First Example","Second Example"]}

		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
	}
	
	@Test
	public void saveClientTest() throws Exception {
		
		String clientJsonWithoutID = "{\"name\": \"TCR\",\"type\": {\"clientTypeDesc\": \"KEY\",\"id\": 1}}";
		
		Mockito.when(
				clientDaoService.save(Mockito.any(ClientDto.class))).thenReturn(mockClient);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/clients")
				.header("Authorization", "Bearer " + Constants.TEST_TOKEN)
				.accept(MediaType.APPLICATION_JSON)
				.content(clientJsonWithoutID)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), Matchers.either(Matchers.is(HttpStatus.OK.value())).or(Matchers.is(HttpStatus.CREATED.value())));
	}
	
	@Test
	public void updateClientTest() throws Exception {
		
		String clientJsonWithID = "{\"id\":1,\"name\": \"TCR\",\"type\": {\"clientTypeDesc\": \"KEY\",\"id\": 1}}";
		mockClient.setId(1);
		mockClient.setName("FCR");
		ClientType type = new ClientType();
		type.setId(1);
		type.setClientTypeDesc("KEY");
		mockClient.setType(type);
		
		Mockito.when(
				clientDaoService.save(Mockito.any(Client.class))).thenReturn(mockClient);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/clients")
				.header("Authorization", "Bearer " + Constants.TEST_TOKEN)
				.accept(MediaType.APPLICATION_JSON)
				.content(clientJsonWithID)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), Matchers.either(Matchers.is(HttpStatus.OK.value())).or(Matchers.is(HttpStatus.CREATED.value())));
	}
	
	
	
	
	private String createURLWithPort(String uri) {
		System.out.println("/**************************8:::" + (contextPath + ":" + port + uri));
		return contextPath + ":" + port + uri;
	}*/
}
