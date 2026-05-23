package nonamecrackers2.witherstormmod.client.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Scanner;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.locating.IModFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WitherStormModelDecoder {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final int VERSION = 0;

   public static void load(String id, CubeListBuilder builder, CubeDeformation def, float texScale) {
      IModFile modFile = ModList.get().getModFileById("witherstormmod").getFile();
      Path path = modFile.findResource(new String[]{"assets/witherstormmod/models/witherstorm/" + id + ".wsm"});
      File file = new File(path.toString());

      try {
         Scanner scanner = new Scanner(file);
         boolean checkedVersion = false;

         while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (checkedVersion) {
               String[] functions = line.split(";");

               for (String function : functions) {
                  try {
                     if (function.startsWith("texOffs")) {
                        String param = function.replaceAll("texOffs(", "").replaceAll(")", "");
                        String[] values = param.split(",");
                        int texX = Integer.valueOf(values[0]);
                        int texY = Integer.valueOf(values[1]);
                        builder.texOffs(texX, texY);
                     } else if (function.startsWith("addBox")) {
                        String param = function.replaceAll("addBox(", "").replaceAll(")", "");
                        String[] values = param.split(",");
                        float f = Float.valueOf(values[0]);
                        float f1 = Float.valueOf(values[0]);
                        float f2 = Float.valueOf(values[0]);
                        float f3 = Float.valueOf(values[0]);
                        float f4 = Float.valueOf(values[0]);
                        float f5 = Float.valueOf(values[0]);
                        builder.addBox(f, f1, f2, f3, f4, f5, def, texScale, texScale);
                     } else {
                        LOGGER.warn("Unknown function '{}'. Ignoring", function);
                     }
                  } catch (Exception var23) {
                     LOGGER.error("Failed to parse function");
                     var23.printStackTrace();
                  }
               }
            } else if (line.startsWith("v")) {
               try {
                  if (Integer.valueOf(line.split("v")[0]) != 0) {
                     LOGGER.error("Outdated model version, please update to {}", 0);
                     break;
                  }

                  checkedVersion = true;
               } catch (Exception var24) {
                  LOGGER.error("Failed to parse version for '{}'", id);
                  var24.printStackTrace();
                  break;
               }
            } else {
               LOGGER.error("Invalid version string. Must be of format 'v{version}'");
            }
         }

         scanner.close();
      } catch (FileNotFoundException var25) {
         LOGGER.warn("Failed to find model with id '{}'", id);
         var25.printStackTrace();
      }
   }
}
