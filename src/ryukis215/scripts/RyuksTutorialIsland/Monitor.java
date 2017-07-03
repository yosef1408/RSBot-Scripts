package ryukis215;

import static org.powerbot.script.Condition.sleep;

/**
 * 
 * Provides methods of monitoring stages
 * 
 * @author Ryukis215
 *
 */
public class Monitor extends Controller {
	
	
	static int stage = 1;
	
	/**
	 * if flashing icon seen, click it
	 */
	public void monitorFlashingIcons(){
		
		//spanner
		if(v.varpbit(281) == 3 && v.varpbit(1021) == 12)
			w.widget(548).component(34).click();
		
		//spanner x2
		if(v.varpbit(281) == 190 && v.varpbit(1021) == 12)
			w.widget(548).component(34).click();	
		
		//inventory
		if(v.varpbit(281) == 30 && v.varpbit(1021) == 4)
			w.widget(548).component(50).click();
		
		//stats
		if(v.varpbit(281) == 60 && v.varpbit(1021) == 2)
			w.widget(548).component(48).click();
		
		//harp
		if(v.varpbit(281) == 170 && v.varpbit(1021) == 14)
			w.widget(548).component(36).click();
			
		//emotes
		if(v.varpbit(281) == 183 && v.varpbit(1021) == 13)
			w.widget(548).component(35).click();
			
		//quest
		if(v.varpbit(281) == 230 && v.varpbit(1021) == 3)
			w.widget(548).component(49).click();
			
		//armour
		if(v.varpbit(281) == 390 && v.varpbit(1021) == 5)
			w.widget(548).component(58).click();
			
		//combat
		if(v.varpbit(281) == 430 && v.varpbit(1021) == 1)
			w.widget(548).component(54).click();
			
		//prayer
		if(v.varpbit(281) == 560 && v.varpbit(1021) == 6)
			w.widget(548).component(52).click();
		
		//friends
		if(v.varpbit(281) == 580 && v.varpbit(1021) == 9)
			w.widget(548).component(38).click();
			
		//ignore
		if(v.varpbit(281) == 590 && v.varpbit(1021) == 10)
			w.widget(548).component(39).click();
			
		//magic
		if(v.varpbit(281) == 630 && v.varpbit(1021) == 7)
			w.widget(548).component(53).click();
		
	}
	
	/**
	 * monitors the proggress bar to return set our current stage
	 */
	public void monitorStage(){	
		for(int i = 1; i < 20; i++){
			if(w.widget(371).component(i).textColor() != 0){
				stage = i+1;		
			}else{
				return;
			}
		}
	}
	
	/**
	 * if "Click here to continue" is seen, space bar is pressed
	 * or relative action is taken
	 */
	public void monitorChat(){	
		if (c.canContinue()) {
			ctx.input.send("{VK_SPACE down}");
			sleep(100);
			ctx.input.send("{VK_SPACE up}");
		}
		
		if(w.widget(162).component(33).visible())
			w.widget(162).component(33).click();
	}
	

}
