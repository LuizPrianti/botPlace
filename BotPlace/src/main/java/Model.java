import java.util.LinkedList;
import java.util.List;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

public class Model implements Subject{
	
	private List<Observer> observers = new LinkedList<Observer>();
	
	private List<Instituicao> instituicao = new LinkedList<Instituicao>();
	
	private static Model uniqueInstance;
	
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
		this.instituicao.add(instituicao);
	}
	
	

}
