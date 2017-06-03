package ryukis215;

import java.awt.Point;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.Player;

public class Antiban extends Controller {
	
	public void hoverOverRandomPlayer(){
		Player randPlayer = ctx.players.select().nearest().poll();
		ctx.camera.turnTo(randPlayer);
		randPlayer.hover();
		Condition.sleep(Random.nextInt(1000, 2000));
	}
	
	public void randomCameraTurn(){
		int randNum = Random.nextInt(1, 2);
		if(randNum == 1){//light camera turn
			ctx.camera.angle(ctx.camera.yaw() + Random.nextInt(-100, 100));
		}
		if(randNum == 2){//aggressive camera turn
			randNum = Random.nextInt(2, 6);
			for(int i = 0; i<randNum; i++){
				ctx.camera.angle(ctx.camera.yaw() + Random.nextInt(-100, 100));
				Condition.sleep(Random.nextInt(250, 500));
			}
		}
	}
	
	public void moveMouseOffScreen(){
		int x = Random.nextInt(-1000, -100);
		int y = Random.nextInt(-1000, -100);
		ctx.input.move(x, y);
		ctx.input.defocus();
		Condition.sleep(Random.nextInt(5000, 13000));
		ctx.input.focus();
		x = Random.nextInt(16, 512);
		y = Random.nextInt(45, 334);
		ctx.input.move(x, y);
	}
		
	/**
	* Author - Enfilade Moves the mouse a random distance between minDistance
	* and maxDistance from the current position of the mouse by generating
	* random vector and then multiplying it by a random number between
	* minDistance and maxDistance. The maximum distance is cut short if the
	* mouse would go off screen in the direction it chose.
	*
	* @param minDistance
	* The minimum distance the cursor will move
	* @param maxDistance
	* The maximum distance the cursor will move (exclusive)
	*/
	public void randomMouseMovement(int minDistance, int maxDistance) {
		double xvec = Math.random();
		if (Random.nextInt(0, 2) == 1) {
			xvec = -xvec;
		}
		double yvec = Math.sqrt(1 - xvec * xvec);
		if (Random.nextInt(0, 2) == 1) {
			yvec = -yvec;
		}
		double distance = maxDistance;
		Point p = ctx.input.getLocation();
		int maxX = (int) Math.round(xvec * distance + p.x);
		distance -= Math.abs((maxX - Math.max(0,
				Math.min(ctx.game.dimensions().getWidth(), maxX)))
				/ xvec);
		int maxY = (int) Math.round(yvec * distance + p.y);
		distance -= Math.abs((maxY - Math.max(0,
				Math.min(ctx.game.dimensions().getHeight(), maxY)))
				/ yvec);
		if (distance < minDistance) {
			return;
		}
		distance = Random.nextInt(minDistance, (int) distance);
		ctx.input.move((int) (xvec * distance) + p.x, (int) (yvec * distance) + p.y);
	}
	
	public void checkXP(){
		ctx.widgets.widget(548).component(48).click();
		Condition.sleep(Random.nextInt(300, 500));
		if (ctx.widgets.widget(320).component(19).valid()) {
			ctx.widgets.widget(320).component(19).hover();
			Condition.sleep(Random.nextInt(1000, 3000));
			ctx.widgets.widget(548).component(50).click();
		}
	}

}
