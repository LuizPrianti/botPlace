import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.nativequery.example.Student;

public class Model implements Subject{
	
	private List<Observer> observers = new LinkedList<Observer>();
	
	private List<Instituicao> instituicao = new LinkedList<Instituicao>();
	
	private static Model uniqueInstance;
	
	ObjectContainer locals = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "bd/locals.db4o");

	
	private Model(){}
	
	public static Model getInstance(){
		
		if(uniqueInstance == null){
			uniqueInstance = new Model();
		}
		return uniqueInstance;
	}
	
	public void registerObserver(Observer observer){
		observers.add(observer);
	}
	
	public void notifyObservers(long chatId, String instituicaoData){
		for(Observer observer:observers){
			observer.update(chatId, instituicaoData);
		}
	}
	
	public void addInstituicao(Instituicao instituicao){
		locals.store(instituicao);
		locals.commit();
		
	}
	
	

}
