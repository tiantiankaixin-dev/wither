package nonamecrackers2.witherstormmod.client.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList.Builder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Contributors {
   public static final Logger LOGGER = LogManager.getLogger();
   private static final String CONTRIBUTORS_URL = "https://drive.google.com/uc?export=view&id=1GAFSqpBucUPP_d5QCEfBD7bwkSCRYU5O";
   private static final Gson GSON = new Gson();
   private static final boolean READ_FROM_DRIVE = false;
   private static List<String> DEVELOPERS = ImmutableList.<String>of();
   private static List<String> PATRONS = ImmutableList.<String>of();
   private static List<String> KOFI = ImmutableList.<String>of();
   private static boolean IS_PATRON_ONLY_BUILD = false;
   private static List<String> builtInContributors;

   public static void getContributors() {
      try {
         Builder<String> devs = ImmutableList.builder();
         Builder<String> patrons = ImmutableList.builder();
         Builder<String> kofi = ImmutableList.builder();
         readContributors(line -> {
            String[] split = line.split(Pattern.quote("."));
            if (split.length == 2) {
               if (split[0].equals("dev")) {
                  devs.add(split[1]);
               }

               if (split[0].equals("patron")) {
                  patrons.add(split[1]);
               }

               if (split[0].equals("kofi")) {
                  kofi.add(split[1]);
               }
            } else {
               LOGGER.warn("Entry must be of format 'type.username'");
            }
         });
         DEVELOPERS = devs.build();
         PATRONS = patrons.build();
         KOFI = kofi.build();
      } catch (IOException var3) {
         LOGGER.warn("Failed to fetch contributors.txt");
         var3.printStackTrace();
      }
   }

   private static void readContributors(Consumer<String> consumer) throws IOException {
      if (builtInContributors == null) {
         builtInContributors = Lists.newArrayList();
         builtInContributors.add("dev.Dev");
         builtInContributors.add("patron.Dev");
         builtInContributors.add("kofi.Dev");
         builtInContributors.add("dev.nonamecrackers2");
         builtInContributors.add("dev.WONazareth");
         builtInContributors.add("dev.TCBlizz707");
         builtInContributors.add("dev.TheFrostySoul");
         builtInContributors.add("patron.GEST2122");
         builtInContributors.add("patron.ThePandaMan69420");
         builtInContributors.add("patron.WizardOfZinks");
         builtInContributors.add("patron.WizardPilot7515");
         builtInContributors.add("patron.Nov1st2k10");
         builtInContributors.add("patron.McFella");
         builtInContributors.add("patron.Darkheart78");
         builtInContributors.add("patron.Kraken56YT");
         builtInContributors.add("patron.MDtheDemonmaster");
         builtInContributors.add("patron.MegaMathew200");
         builtInContributors.add("patron.LightningRod2441");
         builtInContributors.add("patron.Tordx_");
         builtInContributors.add("patron.Yamper1163");
         builtInContributors.add("patron.SmoothWolf20");
         builtInContributors.add("patron.sonicfan9353");
         builtInContributors.add("patron.brettge");
         builtInContributors.add("patron.Doggyboy200");
         builtInContributors.add("patron.itsOutares");
         builtInContributors.add("patron.Tibus0119");
         builtInContributors.add("patron.DaSenate");
         builtInContributors.add("patron.Romeo_The_Admin");
         builtInContributors.add("patron.MRN22");
         builtInContributors.add("patron.DairyCamera");
         builtInContributors.add("patron.minecrafthero11");
         builtInContributors.add("patron.Kingofcheeze");
         builtInContributors.add("patron.Malolve");
         builtInContributors.add("patron.EuroControl");
         builtInContributors.add("patron.RayJasi11");
         builtInContributors.add("patron.BLU3_ZOMB1EX");
         builtInContributors.add("patron.NOTNA9015");
         builtInContributors.add("patron.Auxi0us");
         builtInContributors.add("patron.Joebiben1");
         builtInContributors.add("patron.RelliGin");
         builtInContributors.add("patron.RippedRumble806");
         builtInContributors.add("patron._ThatBoi_");
         builtInContributors.add("kofi.WONazareth");
         builtInContributors.add("kofi.nonamecrackers2");
         builtInContributors.add("kofi.Parm_on_John");
         builtInContributors.add("kofi.Shadow_Parad0x");
         builtInContributors.add("kofi.Doggyboy200");
         builtInContributors.add("kofi.GEST2122");
         builtInContributors.add("kofi.Tibus0119");
         builtInContributors.add("kofi.DE4D_Gamez");
      }

      for (String s : builtInContributors) {
         consumer.accept(s);
      }
   }

   public static boolean isDeveloper(String name) {
      return DEVELOPERS.contains(name);
   }

   public static boolean isPatron(String name) {
      return PATRONS.contains(name);
   }

   public static boolean isKofi(String name) {
      return KOFI.contains(name);
   }

   public static boolean currentPlayerHasCosmetic() {
      Minecraft mc = Minecraft.getInstance();
      String name = mc.getUser().getName();
      return isDeveloper(name) || isPatron(name);
   }

   @Deprecated
   public static Contributors.Result getAccess(String uuid) {
      if (IS_PATRON_ONLY_BUILD && FMLEnvironment.production) {
         try {
            URL url = new URL("https://patronauthenticator-sp4uwbgqwa-uc.a.run.app/validate?mc_uuid=" + uuid);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int status = connection.getResponseCode();
            Reader streamReader;
            if (status >= 300) {
               streamReader = new InputStreamReader(connection.getErrorStream());
            } else {
               streamReader = new InputStreamReader(connection.getInputStream());
            }

            BufferedReader in = new BufferedReader(streamReader);
            Contributors.Result result = Contributors.Result.NOT_VALIDATED;

            String line;
            while ((line = in.readLine()) != null) {
               JsonObject object = (JsonObject)GSON.fromJson(line, JsonObject.class);
               LOGGER.info("Received: " + object);
               if (object.has("result")) {
                  if (!GsonHelper.getAsString(object, "result").equals("validated")) {
                     continue;
                  }

                  result = Contributors.Result.VALIDATED;
                  break;
               }

               result = Contributors.Result.ERROR;
               break;
            }

            in.close();
            connection.disconnect();
            return result;
         } catch (Exception var9) {
            LOGGER.error("Failed to validate user", var9);
            return Contributors.Result.ERROR;
         }
      } else {
         return Contributors.Result.NO_RESTRICTIONS;
      }
   }

   public static enum Result {
      VALIDATED(true),
      NO_RESTRICTIONS(true),
      NOT_VALIDATED(false),
      ERROR(false);

      private boolean canLaunchGame;

      private Result(boolean canLaunchGame) {
         this.canLaunchGame = canLaunchGame;
      }

      public boolean canLaunchGame() {
         return this.canLaunchGame;
      }
   }
}
