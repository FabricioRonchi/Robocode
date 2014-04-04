package John_Dillinger;

import robocode.*;
import robocode.util.Utils;
import java.awt.Color;

public class John_Dillinger extends AdvancedRobot {

    private int count = 0;
    private double ultimaEnergia = 0;
    private double dir = 1;
	private double movimento;
	
    public void run() {
        setAllColors(Color.BLACK);
        setBulletColor(Color.WHITE);
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);      
        setTurnRadarRightRadians(Math.PI);
        while (true) {
	       	if (getDistanceRemaining() == 0 && getTurnRemaining() == 0) {
	        	if (count % 50 < 8){
	            	setAhead(30*dir);
	     		}else{
	            setAhead(10*dir);
	            	if (count % 50 < 8) {
	                    setTurnLeft(10);
	                } else {
	                    setTurnRight(10);
	                }
	            }
			}
	       	execute();
	        scan();           
	      	if (getRadarTurnRemaining() == 0) {
	       		setTurnRadarLeft(Double.POSITIVE_INFINITY);
	       	}    
	    }
	}

    public void onScannedRobot(ScannedRobotEvent e) {               

        double power = 3;
        double d = e.getDistance();
        if (d < 100){
            power = 3;		
        }else if (d < 200 && d > 200){
			power = 2;			
		}else{
			power = 1;
		}          

        double v = d / power;
        double posicaoOponente = getHeadingRadians() + e.getBearingRadians() - getRadarHeadingRadians();  
		//Rediciona  Radar ao Oponenete     
        setTurnRadarRightRadians(Utils.normalRelativeAngle(posicaoOponente * power));
		//Rediciona  Radar ao Oponenete        
      	double posicaoCanhaoY = getHeadingRadians() + e.getBearingRadians() - getGunHeadingRadians();
        double posicaoCanhaoX = e.getHeadingRadians() - (getHeadingRadians() + e.getBearingRadians());
        posicaoCanhaoY = posicaoCanhaoY + (e.getVelocity() * posicaoCanhaoX / v);
        setTurnGunRightRadians(posicaoCanhaoY);    			
       	setFire(power);       		    		                      
        double energiaPerdida = ultimaEnergia - e.getEnergy();
        ultimaEnergia = e.getEnergy();
		if (energiaPerdida < 3 && energiaPerdida > 1) {           
            movimento = e.getBearing() + 60;      
            setTurnRight(movimento);           
            setAhead(dir);
        } else if (d > 100) {           
            movimento = e.getBearing() + 25;
            setTurnRight(movimento);            
            setAhead(50*dir);
        } else {         
            movimento = e.getBearing() + 90;
            setTurnRight(movimento);
            setAhead(30*dir);           
        }     
        execute();
        scan();
    }

    public void onHitByBullet(HitByBulletEvent e) {
        dir *= 1;
    }

    public void onHitWall(HitWallEvent e) {
        dir *= 1;
    }
}
