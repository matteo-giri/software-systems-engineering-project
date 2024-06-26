package rx
 
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import alice.tuprolog.Term
import alice.tuprolog.Struct
import unibo.basicomm23.interfaces.IApplMessage
import unibo.basicomm23.utils.CommUtils


class distanceFilter (name : String ) : ActorBasic( name ) {
val DLIMT = 40
//@kotlinx.coroutines.ObsoleteCoroutinesApi

    override suspend fun actorBody(msg: IApplMessage) {
		if( msg.msgId() != "sonardata") return
		if( msg.msgSender() == name) return //AVOID to handle the event emitted by itself
  		elabData( msg )
 	}

 	
//@kotlinx.coroutines.ObsoleteCoroutinesApi

	  suspend fun elabData( msg: IApplMessage ){ //OPTIMISTIC
		  // if( msg.msgId() == "sonardata" ) return; //avoid ...
 		val data  = (Term.createTerm( msg.msgContent() ) as Struct).getArg(0).toString()
		val Distance = Integer.parseInt( data )
/*
 * Emit a sonarRobot event to test the behavior with MQTT
 * We should avoid this pattern
*/	
 	 	//val m0 = MsgUtil.buildEvent(name, "sonardata", "distance($data)")
		//  CommUtils.outgreen("$tt $name |  emits = $m0 ")
		  //emit( m0 )
 		if( Distance > 0 && Distance < DLIMT ){
	 		val m1 = MsgUtil.buildEvent(name, "obstacle", "obstacle($data)")
			//CommUtils.outgreen("$tt $name |  emitLocalStreamEvent m1= $m1")
			emitLocalStreamEvent( m1 ) //propagate event obstacle
     	}else{
			val m2 = MsgUtil.buildEvent(name, "free", "free($data)")
			//CommUtils.outgreen("$tt $name |  emitLocalStreamEvent m2= $m2")
			emitLocalStreamEvent( m2 ) //propagate event free
 		}				
 	}
}