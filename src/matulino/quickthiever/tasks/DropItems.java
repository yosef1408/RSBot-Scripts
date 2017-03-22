package matulino.quickthiever.tasks;

import org.powerbot.script.rt4.Npc;

import java.util.Iterator;
import java.util.concurrent.Callable;


import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.ItemQuery;
import org.powerbot.script.rt4.Npc;


public class DropItems extends Task<ClientContext> {

	private QuickThiever main;
	private String eat = "Eat";
	private ItemQuery<Item> items;
	public DropItems(ClientContext ctx, QuickThiever main) {
		super(ctx);
		this.main = main;
	}

	@Override
	public boolean activate() {
		
		return (ctx.inventory.select().count() > 27) && (itemsToDrop().size() >=  1);
	}

	@Override
	public void execute() {
		main.status = "Dropping junk";
		
		
			
		if (items != null) {
			
			dropItems(items);
			
		}
	}
	
	

	private boolean dropItems(ItemQuery<Item> items) {
		  if((ctx.varpbits.varpbit(1055) & 131072) > 0) {  // if shift-dropping Enabled
		    ctx.input.send("{VK_SHIFT down}");
		    for(Item i : items) {
		      if(!i.click(true)) {
		        ctx.input.send("{VK_SHIFT up}");
		        return false;
		      }
		    }
		    ctx.input.send("{VK_SHIFT up}");
		    return true;
		  }
		  for(Item i : items)
		    if(!i.interact("Drop"))
		      return false;
		  return true;
	}
	
	
	private  ItemQuery itemsToDrop() { 
		items = ctx.inventory.select(new Filter<Item>(){
			@Override
			public boolean accept(Item i) {
				
				try {
					return ((!i.actions()[0].equals(eat)));
				}
				catch (NullPointerException e) {
					if ((i.name() != null) && !main.keptItems.contains(i.name())) return true;
					return false;
				}}});
		return items;
		
	}

}
