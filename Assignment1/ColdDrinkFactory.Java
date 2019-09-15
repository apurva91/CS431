// importing required packages.
import java.util.concurrent.*; 
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

// bottle class

class Bottle {
	// type of bottle.
	int type;
	// is bottle sealed.
	int isSealed;
	// is bottle packaged.
    int isPackaged;
	
	// initializer function for the Bottle class.
	Bottle(int type) {
		this.type = type;
		this.isSealed = 0;
        this.isPackaged = 0;
	}

}

// unfinished tray class

class UnfinishedTray{
	//	semaphore lock for unfinished tray.
	Semaphore sema = new Semaphore(1);
	//	no. of bottles of type 1 in tray.
	int noOfBottle1;
	//	no. of bottles of type 2 in tray.
	int noOfBottle2;
	//	initializer function for UnfinishedTray class.
	UnfinishedTray(int noOfBottle1, int noOfBottle2){
		this.noOfBottle1=noOfBottle1;
		this.noOfBottle2=noOfBottle2;
	}
	//	method to return specific type of bottle from unfinished class.
	Bottle getBottle(int i){
		try{
			sema.acquire();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		if(i==1){
			if(noOfBottle1>0){
				Bottle bottle = new Bottle(1);
				//	decreasing number of bottle 1.
				noOfBottle1--;
				sema.release();
				return bottle;
			}
			sema.release();
			//	no bottle left.
			return null;
		}
		else{
			if(noOfBottle2>0){
				Bottle bottle = new Bottle(2);
				//	decreasing number of bottle 2.
				noOfBottle2--;
				sema.release();
				return bottle;
			}
			sema.release();
			//	no bottle left.
			return null;
		}
		
	}
}

//	class for tray of finished bottles.

class Finished{
	// semaphore lock for access to finished tray.
	Semaphore sema = new Semaphore(1);
	// no. of finished bottles of type 1.
	int noOfBottle1;
	// no. of finished bottles of type 2.
	int noOfBottle2;
	//	initializing tray of finished bottles.
	Finished(){
		this.noOfBottle1=0;
		this.noOfBottle2=0;
	}
	//	adding to finished tray.
	void send(int i){
		try{
			sema.acquire();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		if(i==1){
			this.noOfBottle1++;
		}
		else{
			this.noOfBottle2++;
		}
		sema.release();
	}
}

// class for packaging module.

class Packager {
	//	tray 1 for packager storing bottles of type 1.
	List<Bottle> tray1;
	//	tray 2 for packager storing bottles of type 2.
	List<Bottle> tray2;
	// internal timer for packager.
	int time;
	//	status of operation of packager.
	int status;
	//	next type of bottle to be taken from unfinished tray.
	int flip1;
	//	next type of bottle to be taken from packager's tray.
	int flip2;
	//	current bottle getting packaged.
	Bottle curr;
	//	initializing packager object.
	Packager(){
		
		this.time=0;
		this.tray1 = new ArrayList<Bottle>();
		this.tray2 = new ArrayList<Bottle>();
		this.status = 0;
		this.flip1 = 1;
		this.flip2 = 1;
		this.curr = null;
	}
}

// class for sealing module.

class Sealer {
	//	tray for sealer.
	List<Bottle> tray;
	//	internal timer for sealer.
	int time;
	//	status of operation of sealer.
	int status;
	//	current bottle getting sealed.
	Bottle curr;
	//	next type of bottle to be taken from unfinished tray.
	int count;
	//	initializing sealer object.
	Sealer(){
		this.status = 0;
		this.time=0;
		this.count = 2;
		this.tray = new ArrayList<Bottle>();
		this.curr = null;
	}	
	
}

//	thread class for getting bottle from packager to sealer.

class PackagerToSealerThread extends Thread {
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

// class for thread moving bottle from sealer to packager's tray.

class SealerToPackagerThread extends Thread {
	//	sealer object.
	Sealer sealer;
	//	global time.
	int time;
	//	packager object.
	Packager packager;
	//	finished tray object.
	Finished finished;
	//	initializing the thread.
	SealerToPackagerThread(Packager packager, Sealer sealer, int time, Finished finished){
		this.sealer = sealer;
		this.time = time;
		this.packager = packager;
		this.finished = finished;
	}
	@Override
	public void run(){
		//	if sealer has stopped processing on or before global time.
		if(sealer.time<=this.time){
			//	if sealer is carrying newly sealed bottle.
			if(sealer.status == 1){
				//	turning isSealed to true for the bottle.
				sealer.curr.isSealed = 1;
				//	if bottle is also packaged, then move to godown.
				if(sealer.curr.isPackaged == 1){
					
					if(sealer.curr.type == 1){
						finished.send(1);
					}
					else{
						finished.send(2);
					}
					sealer.status = 0;
				}
				else if(sealer.curr.type == 1){
					
					if(packager.tray1.size()<2){
						
						packager.tray1.add(sealer.curr);
						sealer.status = 0;
					}
					
				}
				//	move the bottle to packager's tray.
				else{
					
					if(packager.tray2.size()<3){
						
						packager.tray2.add(sealer.curr);
						sealer.status = 0;
					}
				}
			}
		}
	}
}

//	thread for moving bottles into packager from trays.

class TrayToPackagerThread extends Thread {
	//	packager object.
	Packager packager;
	//	global time.
	int time;
	//	unfinished tray object.
	UnfinishedTray unfinished;
	//	initializing the thread.
	TrayToPackagerThread(Packager packager, int time, UnfinishedTray unfinished){
		this.packager = packager;
		this.time = time;
		this.unfinished = unfinished;
	}
	@Override
	public void run(){
		//	packager is idle and contains no bottle in it.
		if(packager.status == 0){
			//	atleast one of the trays is non-empty.
			if(packager.tray1.size() > 0 || packager.tray2.size() > 0){
				//	input from tray 1.
				if(packager.flip2==1){
					if(packager.tray1.size()>0){
						packager.curr = packager.tray1.get(0);
						packager.tray1.remove(0);
						packager.status = 1;	
					}
					else{
						packager.curr = packager.tray2.get(0);
						packager.tray2.remove(0);
						packager.status = 1;
					}
					packager.flip2=2;
				}
				//	input from tray 2;
				else{
					if(packager.tray2.size()>0){
						packager.curr = packager.tray2.get(0);
						packager.tray2.remove(0);
						packager.status = 1;	
					}
					else{
						packager.curr = packager.tray1.get(0);
						packager.tray1.remove(0);
						packager.status = 1;
					}
					packager.flip2=1;
				}
			}
			//	taking input from unfinished tray.
			else{
				//	take bottle of type 1.
				if(packager.flip1==1){
					Bottle x = unfinished.getBottle(1);
					if(x!=null){
						
						packager.curr = x;
						packager.status = 1;
					}
					else{
						x = unfinished.getBottle(2);
						if(x!=null){
						packager.curr = x;
						packager.status = 1;
					}
					}
					packager.flip1 = 2;
				}
				//	take bottle of type 2.
				else{
					Bottle x = unfinished.getBottle(2);
					if(x!=null){
						packager.curr = x;
						packager.status = 1;
					}
					else{
						x = unfinished.getBottle(1);
						if(x!=null){
						packager.curr = x;
						packager.status = 1;
					}
					}
					packager.flip1 = 1;
				}
			}
			//	increasing timer of packager by 2.
			packager.time = packager.time + 2;
		}
	}
}

//	thread for moving bottles into sealer from trays.

class TrayToSealerThread extends Thread {
	//	sealer object.
	Sealer sealer;
	//	global time.
	int time;
	//	unfinished tray object.
	UnfinishedTray unfinished;
	//	initialzing the thread.
	TrayToSealerThread(Sealer sealer, int time, UnfinishedTray unfinished){
		this.sealer = sealer;
		this.time = time;
		this.unfinished = unfinished;
	}
	@Override
	public void run(){
		//	sealer is idle and contains no bottle in it.
		if(sealer.status == 0){
			//	sealer's tray is non-empty.
			if(sealer.tray.size()>0){
				sealer.curr = sealer.tray.get(0);
				sealer.tray.remove(0);
				sealer.status = 1;
			}
			else{
				//	taking bottle of type 1 from unfinished tray.
				if(sealer.count==1){
					Bottle x = unfinished.getBottle(1);
					if(x!=null){
						sealer.curr = x;
						sealer.status = 1;
					}
					else{
						x = unfinished.getBottle(2);
						if(x!=null){
							sealer.curr = x;
							sealer.status = 1;
						}
					}
					sealer.count=2;
					
				}
				//	taking bottle of type 2 from unfinished tray.
				else{
					Bottle x = unfinished.getBottle(2);
					if(x!=null){
						sealer.curr = x;
						sealer.status = 1;
					}
					else{
						x = unfinished.getBottle(1);
						if(x!=null){
							sealer.curr = x;
							sealer.status = 1;
						}
					}
					sealer.count=1;
				}
			}
			sealer.time = sealer.time + 3;
		}
	}
}

class Main{
    //	global timer for the machine.
    private static int time=0;
	
    public static void main(String[] args) throws IOException{
		//	number of bottles of type 1.
        int noOfBottle1 = Integer.parseInt(args[0]);
		//	number of bottles of type 2.
        int noOfBottle2 = Integer.parseInt(args[1]);
		//	total time for machine to run. 
        int total_time = Integer.parseInt(args[2]);
		//	unfinished tray object.
		UnfinishedTray unfinished = new UnfinishedTray(noOfBottle1, noOfBottle2);
		//	finished tray object.
		Finished finished = new Finished();
		//	packager object.
		Packager packager = new Packager();
		//	sealer object.
		Sealer sealer = new Sealer();
		//	iterating over total_time
		while(time!=total_time+1){
			//	thread for taking packaged bottles to sealer.
			PackagerToSealerThread t1 = new PackagerToSealerThread(packager, sealer, time, finished);
			//	thread for taking sealed bottles to packager.
			SealerToPackagerThread t2 = new SealerToPackagerThread(packager, sealer, time, finished);
			//	starting threads.
			t1.start();
			t2.start();
			//	sleeping to let the threads complete.
			try
			{	
    			Thread.sleep(100);
			}
			catch(InterruptedException ex){
    			Thread.currentThread().interrupt();
			}
			//	thread for taking input into packager from tray.
			TrayToPackagerThread t3 = new TrayToPackagerThread(packager, time, unfinished);
			//	thread for taking input into sealer from tray.
			TrayToSealerThread t4 = new TrayToSealerThread(sealer, time, unfinished);
			//	starting threads.
			t3.start();
			t4.start();
			//	sleeping to let the threads complete.
			try
			{
    			Thread.sleep(100);
			}
			catch(InterruptedException ex)
			{
    			Thread.currentThread().interrupt();
			}	
			//	incrementing the time.
			time++;
		}
		
		//	counting different packaged and sealed bottles.
		int noOfBottle1Packaged=0,noOfBottle1Sealed=0,noOfBottle1InGodown=0,noOfBottle2Packaged=0,noOfBottle2Sealed=0,noOfBottle2InGodown=0;
		//	looking into tray 1 of packager.
		for(int i=0;i<packager.tray1.size();i++){
			if(packager.tray1.get(i).isSealed==1){noOfBottle1Sealed++;}
		}
		//	looking into tray 2 of packager.
		for(int i=0;i<packager.tray2.size();i++){
			if(packager.tray2.get(i).isSealed==1){noOfBottle2Sealed++;}
		}
		if(packager.curr!=null && packager.status==1){
			if(packager.curr.isSealed==1){
			if(packager.curr.type==1){
				noOfBottle1Sealed++;
			}
			else{
				noOfBottle2Sealed++;
			}
			}
		}
		//	looking into tray of sealer.
		for(int i=0;i<sealer.tray.size();i++){
			if(sealer.tray.get(i).type==1){
				if(sealer.tray.get(i).isPackaged==1)
				noOfBottle1Packaged++;
				}
			else{
				if(sealer.tray.get(i).isPackaged==1)
				noOfBottle2Packaged++;
			}
		}
		//	looking into finished tray.
		noOfBottle1InGodown+=finished.noOfBottle1;
		noOfBottle1Packaged+=finished.noOfBottle1;
		noOfBottle1Sealed+=finished.noOfBottle1;
		noOfBottle2InGodown+=finished.noOfBottle2;
		noOfBottle2Packaged+=finished.noOfBottle2;
		noOfBottle2Sealed+=finished.noOfBottle2;
		if(sealer.curr!=null && sealer.status==1){
			if(sealer.curr.isPackaged==1){
			if(sealer.curr.type==1){
				noOfBottle1Packaged++;
			}
			else{
				noOfBottle2Packaged++;
			}
			}
		}
		//	printing the result.
		System.out.println("Number of Bottle 2 packaged "+noOfBottle1Packaged);
		System.out.println("Number of Bottle 2 sealed "+noOfBottle1Sealed);
		System.out.println("Number of Bottle 2 in godown "+noOfBottle1InGodown);
		System.out.println("Number of Bottle 2 packaged "+noOfBottle2Packaged);
		System.out.println("Number of Bottle 2 sealed "+noOfBottle2Sealed);
		System.out.println("Number of Bottle 2 in godown "+noOfBottle2InGodown);
    }
}