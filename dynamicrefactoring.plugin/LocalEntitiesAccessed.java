package repository.moon.concretefunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import moon.core.entity.Entity;
import moon.core.instruction.Instr;
import refactoring.engine.Function;

/**
 * Gets the set of entities accesed locally in an instruction set.
 * 
 * @author Raúl Marticorena
 * @since JavaMoon-2.3.0
 * @see repository.moon.concretefunction.LocalWrittenEntitiesAccessed
 * @see repository.moon.concretefunction.LocalReadEntitiesAccessed
 */
public class LocalEntitiesAccessed extends Function {
	/**
	 * Instr.
	 */
	private List<Instr> listInstr;

	/**
	 * Constructor.
	 * 
	 * @param classDef class 
	 */
	public LocalEntitiesAccessed(List<Instr> instr) {
		this.listInstr = instr;
	}

	/**
	 * Gets the set of entities of the class, including inheritance.
	 * 
	 * @return entities
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection getCollection() {		

		// As the union of both sets (read and written entities) without duplicates
		Function functionRead = new LocalReadEntitiesAccessed(listInstr);
		List<Entity> listRead = new ArrayList<Entity>(functionRead.getCollection());
		
		
		Function functionWrite = new LocalWrittenEntitiesAccessed(listInstr);
		List <Entity> listWrite = new ArrayList<Entity>(functionWrite.getCollection());
		
		
		List total = new ArrayList();
		
		for (Entity entity : listRead){
			total.add(entity);
		}
		
		for (Entity entity : listWrite){
			if (!total.contains(entity)){
				total.add(entity);
			}
		}
		
		total = sort(total.toArray());
		return total;
		
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Sort alphabetically the entities.
	 * 
	 * @param unsorted entities
	 * @return sorted entities
	 */
	private List<Entity> sort(Object[] entities){
		Entity[] newEntities = new Entity[entities.length];
		for (int i = 0; i<entities.length; i++){
			newEntities[i] = (Entity) entities[i];
		}
		
		for (int i = 0; i < newEntities.length-1; i++){
			for (int j = i+1; j < newEntities.length;j++){
				if (newEntities[i].getName().toString().compareTo(newEntities[j].getName().toString()) > 0){
					Entity a = newEntities[i];
					newEntities[i] = newEntities[j];
					newEntities[j] = a;
				}
			}
		}
		List<Entity> total = new ArrayList<Entity>();
		for (Entity entity : newEntities){
			total.add(entity);
		}
		return total;
	}
}
 