package assignment1.q2;

//	thread class for getting bottle from packager to sealer.
public class PackagerToSealerThread extends Thread {
	//	packager object.
	Packager packager;
	//	global time.
	int time;
	//	sealer object.
	Sealer sealer;
	//	finished tray object.
	Finished finished;
	//	initializing the thread.
	PackagerToSealerThread(Packager packager,Sealer sealer, int time, Finished finished){
		this.packager = packager;
		this.sealer = sealer;
		this.time = time;
		this.finished = finished;
	}
	@Override
	public void run(){
		//	if packager has stopped processing on or before global time.
		if(packager.time<=time){
			//	if packager contains newly packaged bottle.
			if(packager.status==1){
				// setting isPackaged to true.
				packager.curr.isPackaged = 1;
				//	if bottle is also sealed, move it to godown.
				if(packager.curr.isSealed == 1){
					if(packager.curr.type == 1){
						finished.send(1);
					}
					else{
						finished.send(2);
					}
					packager.status = 0;
				}
				//	if sealer's tray has some vacancy, push bottle to the tray.
				else if(sealer.tray.size()<2){
					sealer.tray.add(packager.curr);
					packager.status = 0;
				}
				
			}
		}
	}
}
