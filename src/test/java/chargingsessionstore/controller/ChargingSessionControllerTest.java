package chargingsessionstore.controller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import chargingsessionstore.Application;
import chargingsessionstore.controller.responseobject.ChargingSummary;
import chargingsessionstore.entity.ChargingSession;
import chargingsessionstore.entity.ChargingSessionStatus;
import chargingsessionstore.repository.ChargingSessionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ChargingSessionControllerTest {

	@Autowired
    private MockMvc mvc;
	
	@Autowired
	private ChargingSessionRepository repository;
	
	@After
	public void cleanUp() {
		repository.deleteAll();
	}
	
	@Test
	public void submitNewSessionTest() throws Exception {
		LocalDateTime startedAt = LocalDateTime.now();
		ChargingSession newSession = submitNewSession(startedAt);
		
		assertTrue(newSession.getId() != null);
		assertTrue(newSession.getStartedAt().equals(startedAt));
		assertTrue(newSession.getStatus().equals(ChargingSessionStatus.STARTED));
	}
	
	@Test
	public void submitAndGetSessionTest() throws Exception {
		LocalDateTime startedAt = LocalDateTime.now();
		ChargingSession newSession = submitNewSession(startedAt);
		
		MockHttpServletRequestBuilder request = get("/chargingSessions/" + newSession.getId());
		ResultActions result = mvc.perform(request);
		result.andExpect(status().isOk());
		ChargingSession getResponse = getSessionObjectFromResponse(result);
		
		assertTrue(newSession.getId().equals(getResponse.getId()));
		assertTrue(newSession.getStatus().equals(getResponse.getStatus()));
	}
	
	@Test
	public void submitAndSuspendSessionTest() throws Exception {
		LocalDateTime startedAt = LocalDateTime.now();
		ChargingSession newSession = submitNewSession(startedAt);
		
		LocalDateTime suspendedAt = LocalDateTime.now();
		MockHttpServletRequestBuilder request = put("/chargingSessions/" + newSession.getId());
		request.contentType(MediaType.APPLICATION_JSON);
		request.content("{ \"suspendedAt\": \"" + suspendedAt + "\" }");
		ResultActions result = mvc.perform(request);
		result.andExpect(status().isOk());
		ChargingSession suspended = getSessionObjectFromResponse(result);
		
		assertTrue(suspended.getId().equals(newSession.getId()));
		assertTrue(suspended.getStatus().equals(ChargingSessionStatus.SUSPENDED));
		assertTrue(suspended.getSuspendedAt().equals(suspendedAt));
	}
	
	@Test
	public void submitAndSuspendSessionsAndGetSummaryTest() throws Exception {
		LocalDateTime startedAt = LocalDateTime.now();
		ChargingSession newSession = submitNewSession(startedAt);
		
		startedAt = LocalDateTime.now();
		submitNewSession(startedAt);
		
		startedAt = LocalDateTime.now();
		submitNewSession(startedAt);
		
		LocalDateTime suspendedAt = LocalDateTime.now();
		MockHttpServletRequestBuilder request = put("/chargingSessions/" + newSession.getId());
		request.contentType(MediaType.APPLICATION_JSON);
		request.content("{ \"suspendedAt\": \"" + suspendedAt + "\" }");
		ResultActions result = mvc.perform(request);
		result.andExpect(status().isOk());
		
		request = get("/chargingSummary");
		result = mvc.perform(request);
		result.andExpect(status().isOk());
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		String responseAsString = result.andReturn().getResponse().getContentAsString();
		ChargingSummary summary = objectMapper.readValue(responseAsString, ChargingSummary.class);
		
		assertTrue(summary.getStartedCount().equals(2));
		assertTrue(summary.getSuspendedCount().equals(1));
	}
	
	private ChargingSession submitNewSession(LocalDateTime startedAt) throws Exception {		
		MockHttpServletRequestBuilder request = post("/chargingSessions");
		request.contentType(MediaType.APPLICATION_JSON);
		request.content("{ \"startedAt\": \"" + startedAt + "\" }");
		ResultActions result = mvc.perform(request);
		result.andExpect(status().isOk());
		
		return getSessionObjectFromResponse(result);
	}
	
	private ChargingSession getSessionObjectFromResponse(ResultActions response) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		String responseAsString = response.andReturn().getResponse().getContentAsString();
		
		return objectMapper.readValue(responseAsString, ChargingSession.class);
	}
	
}
