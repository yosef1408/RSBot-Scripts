package sintaax.tutorialisland.engine.constants;

import sintaax.tutorialisland.engine.objects.Context;

import org.powerbot.script.rt4.ChatOption;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ChatOptions extends Context<ClientContext> {
    public ChatOptions(ClientContext ctx) {
        super(ctx);
        chatHashMap.put(0, tutorial);
    }

    public  List<ChatOption> chatList = new ArrayList<>();
    public HashMap<Integer, List<String>> chatHashMap = new HashMap<>();

    private List<String> tutorial = new ArrayList<String>(
            Arrays.asList("I am an experienced player.", "Yes.", "No, I'm not planning to do that."));

    private List<ChatOption> get() {
        final List<ChatOption> options = new ArrayList<ChatOption>(5);
        final Component parent = ctx.widgets.component(Constants.CHAT_WIDGET, 0);

        for (int i = 0; i < 5; i++) {
            final Component component = parent.component(Constants.CHAT_OPTIONS[i]);

            if (!component.valid() || component.textureId() != -1) {
                continue;
            }

            options.add(new ChatOption(ctx, i, component));
        }

        return options;
    }

    private void reset() {
        chatList = new ArrayList<ChatOption>();
    }

    public void update() {
        reset();
        chatList.addAll(get());
    }
}
