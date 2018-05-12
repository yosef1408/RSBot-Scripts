package sintaax.tutorialisland.utilities;

import sintaax.tutorialisland.engine.objects.Context;

import org.powerbot.script.AbstractScript;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Varpbits;

import java.io.*;
import java.sql.Timestamp;

public class VarpbitSpy extends Context<ClientContext> {
    public VarpbitSpy(ClientContext ctx) {
        super(ctx);
    }

    private class Script extends AbstractScript<ClientContext> { }

    private Script s = new Script();
    private int[] vb = new Varpbits(ctx).array();

    public void pollChanges() {
        int[] vbTemp = new Varpbits(ctx).array();

        for (int x = 0; x < vb.length; x++) {
            if (vbTemp[x] != vb[x]) {
                String log = new Timestamp(System.currentTimeMillis())
                        + " >> " + x + ": " + vb[x] + " ->  " + vbTemp[x];

                System.out.println(log);

                try {
                    PrintWriter pw = new PrintWriter(
                            new FileWriter(s.getStorageDirectory() + "/../VarpbitSpy.txt", true));
                    pw.println(log);
                    pw.flush();
                    pw.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        vb = vbTemp;
    }
}
