package seu.lab.dolphin.entity;

import java.util.ArrayList;
import java.util.List;
class Action{
	public String gesture;
	public String function;
	public Action(String gesture,String function){
		this.gesture=gesture;
		this.function=function;
	}
	public String getGesture() {
		return gesture;
	}
	public void setGesture(String gesture) {
		this.gesture = gesture;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
}
public class plug {
	public String name;
	public String instruction;
	List actions=new ArrayList<Action>();
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public List getActions() {
		return actions;
	}
	public void setActions(List actions) {
		this.actions = actions;
	}
	public void add_action(String gesture,String function) {
		this.actions.add(new Action(gesture,function));
	}

	public void set_action(int id,String gesture,String function) {
		this.actions = actions;
	}	

	
}
