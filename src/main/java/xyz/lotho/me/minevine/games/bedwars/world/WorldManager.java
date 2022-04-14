package xyz.lotho.me.minevine.games.bedwars.world;

import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.Location;
import xyz.lotho.me.minevine.games.bedwars.game.BedwarsGame;
import xyz.lotho.me.minevine.games.bedwars.game.GameState;
import xyz.lotho.me.minevine.games.bedwars.generator.Generator;
import xyz.lotho.me.minevine.games.bedwars.generator.GeneratorType;
import xyz.lotho.me.minevine.games.bedwars.team.Team;
import xyz.lotho.me.minevine.games.bedwars.team.TeamType;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.io.IOException;
import java.util.Random;
import java.util.Set;

public class WorldManager {

    private final Minevine instance;
    private final BedwarsGame bedwarsGame;

    public WorldManager(Minevine instance, BedwarsGame bedwarsGame) {
        this.instance = instance;
        this.bedwarsGame = bedwarsGame;
    }

    public Team getNearestBed(Location location) {
        double check = 0;
        Team nearestTeam = null;

        for (Team team : this.bedwarsGame.getTeamManager().getTeams()) {
            double locationDifference = location.distance(team.getSpawnLocation());

            if (check == 0) {
                check = locationDifference;
            }

            if (locationDifference <= check) {
                check = locationDifference;
                nearestTeam = team;
            }
        }

        return nearestTeam;
    }

    public void loadMap(String mapName) {
        SlimeLoader slimeLoader = this.instance.getSlimePlugin().getLoader("mongodb");

        SlimePropertyMap slimePropertyMap = new SlimePropertyMap();
        slimePropertyMap.setInt(SlimeProperties.SPAWN_X, 0);
        slimePropertyMap.setInt(SlimeProperties.SPAWN_Y, 90);
        slimePropertyMap.setInt(SlimeProperties.SPAWN_Z, 0);

        this.instance.getServer().getScheduler().runTaskAsynchronously(this.instance, () -> {
            try {
                if (slimeLoader.isWorldLocked(mapName)) slimeLoader.unlockWorld(mapName);

                SlimeWorld gameMapTemplate = this.instance.getSlimePlugin().loadWorld(slimeLoader, mapName, false, slimePropertyMap);
                SlimeWorld gameMap = gameMapTemplate.clone("game-" + new Random().nextInt(100) + 1);

                this.instance.getServer().getScheduler().runTask(this.instance, () -> {
                    this.instance.getSlimePlugin().generateWorld(gameMap);
                    this.bedwarsGame.setWorld(this.instance.getServer().getWorld(gameMap.getName()));

                    this.loadTeams();
                    this.handleMapSetup();

                    this.bedwarsGame.setGameState(GameState.LOBBY);
                });

                slimeLoader.unlockWorld(mapName);
            } catch (CorruptedWorldException |NewerFormatException | WorldInUseException | UnknownWorldException | IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void loadTeams() {
        for (TeamType value : TeamType.values()) {
            this.bedwarsGame.getTeamManager().addTeam(new Team(this.instance, this.bedwarsGame, value));
        }
    }

    public void handleMapSetup() {
        Set<String> generators = this.instance.getMapsConfiguration().getConfigurationSection("maps.lighthouse.generators").getKeys(false);
        Set<String> teams = this.instance.getMapsConfiguration().getConfigurationSection("maps.lighthouse.teams").getKeys(false);

        generators.forEach(generatorID -> {
            GeneratorType generatorType = GeneratorType.valueOf(this.instance.getMapsConfiguration().getString("maps.lighthouse.generators." + generatorID + ".type"));

            String[] locationSplit = this.instance.getMapsConfiguration().getString("maps.lighthouse.generators." + generatorID + ".location").split(", ");
            Location location = new Location(this.bedwarsGame.getWorld(), Integer.parseInt(locationSplit[0]), Integer.parseInt(locationSplit[1]), Integer.parseInt(locationSplit[2]));

            this.bedwarsGame.getGenerators().add(new Generator(this.instance, bedwarsGame, location, generatorType));
        });

        teams.forEach(teamID -> {
            String[] spawnLocationSplit = this.instance.getMapsConfiguration().getString("maps.lighthouse.teams." + teamID + ".spawn").split(", ");
            Location spawnLocation = new Location(this.bedwarsGame.getWorld(), Integer.parseInt(spawnLocationSplit[0]), Integer.parseInt(spawnLocationSplit[1]), Integer.parseInt(spawnLocationSplit[2]), Float.parseFloat(spawnLocationSplit[3]), Float.parseFloat(spawnLocationSplit[4]));

            Team team = this.bedwarsGame.getTeamManager().getTeam(teamID);
            team.setSpawnLocation(spawnLocation);

            String[] teamGeneratorSplit = this.instance.getMapsConfiguration().getString("maps.lighthouse.teams." + teamID + ".generator").split(", ");
            Location teamGeneratorLocation = new Location(this.bedwarsGame.getWorld(), Integer.parseInt(teamGeneratorSplit[0]), Integer.parseInt(teamGeneratorSplit[1]), Integer.parseInt(teamGeneratorSplit[2]));

            this.bedwarsGame.getGenerators().add(new Generator(this.instance, bedwarsGame, teamGeneratorLocation, GeneratorType.IRON));
            this.bedwarsGame.getGenerators().add(new Generator(this.instance, bedwarsGame, teamGeneratorLocation, GeneratorType.GOLD));
        });
    }

    public void unloadMap() {
        this.instance.getServer().unloadWorld(bedwarsGame.getWorld(), false);
    }
}
