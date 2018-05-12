package sintaax.tutorialisland.engine.modules.zero.tasks;

import sintaax.tutorialisland.engine.Manager;
import sintaax.tutorialisland.engine.constants.ChatOptions;
import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ChatOption;
import org.powerbot.script.rt4.ClientContext;

public class Chatter extends Task<ClientContext> {
    public Chatter(ClientContext ctx) {
        super(ctx);
    }

    private ChatOptions chatOptions = new ChatOptions(ctx);
    private Components components = new Components(ctx);

    @Override
    public boolean activate() {
        return ctx.chat.chatting() || ctx.chat.pendingInput();
    }

    @Override
    public void execute() {
        chatOptions.update();

        if (chatOptions.chatList.size() == 0) {
            if (ctx.chat.canContinue())
                ctx.chat.clickContinue();
            else if (ctx.chat.pendingInput())
                components.ERROR_CONTINUE_BUTTON.click();
        }
        else {
            for (ChatOption chatOption : chatOptions.chatList) {
                for (String string : chatOptions.chatHashMap.get(Manager.activeQuest)) {
                    if (chatOption.text().equals(string)) {
                        ctx.chat.continueChat(string);
                        break;
                    }
                }
            }
        }
    }
}
