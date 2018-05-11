/**
 * @Author Valentin Durand - ENSICAEN
 * @Project CaenBowServer
 * @Package test
 * @Class GameTest
 * @ Dec 8, 2016 10:57:41 AM
 */

import static org.junit.Assert.*;

import org.junit.Test;

import server.*;

import org.json.*;

public class GameTest {

	@Test
	public void testJSONpseudo() {
		Client a = new Client("Toto");
		JSONObject obj = new JSONObject(a.toString());
		if(!obj.has("pseudo"))
			fail("No pseudo in JSON");
	}
	
	@Test
	public void testJSONscore() {
		Client a = new Client("Toto");
		JSONObject obj = new JSONObject(a.toString());
		if(!obj.has("score"))
			fail("No score in JSON");
	}
	
	@Test
	public void testJSONarrows() {
		Client a = new Client("Toto");
		JSONObject obj = new JSONObject(a.toString());
		if(!obj.has("arrows"))
			fail("No arrows in JSON");
	}
	
	@Test
	public void testJSONarray() {
		Client a = new Client("Toto");
		JSONObject obj = new JSONObject(a.toString());
		if(obj.getJSONArray("arrows").length() < 1)
			fail("Empty arrows array in JSON");
	}
	
	@Test
	public void testJSONarrowX() {
		Client a = new Client("Toto");
		JSONObject obj = new JSONObject(a.toString());
		if(obj.getJSONArray("arrows").getJSONObject(0).getInt("x") != 213)
			fail("Wrong x value in arrow JSON");
	}
	
	@Test
	public void testJSONarrowY() {
		Client a = new Client("Toto");
		JSONObject obj = new JSONObject(a.toString());
		if(obj.getJSONArray("arrows").getJSONObject(0).getInt("y") != 233)
			fail("Wrong y value in arrow JSON");
	}
}
