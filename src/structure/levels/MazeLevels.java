package structure.levels;

import structure.models.Cell;
import structure.models.Cell.CellType;
import structure.models.Color;
import structure.models.Position;
import structure.models.State;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class MazeLevels {
    private static String loadLevelFile(String fileName) throws IOException {
        InputStream is = MazeLevels.class.getClassLoader().getResourceAsStream(fileName);
        if (is == null) {
            throw new IOException("Resource file not found: " + fileName + ". Ensure the file is placed in the 'resources' root directory.");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String content = reader.readLine();
            if (content == null) {
                throw new IOException("Resource file " + fileName + " is empty.");
            }
            return content;
        }
    }
    public static State loadLevel1()throws IOException {
//        String levelConfig = loadLevelFile("level2");
        String levelConfig = loadLevelFile("UCS.txt");
        return loadLevel(levelConfig, 10, 10);
    }
    public static State loadLevel(String coordinateString, int maxX, int maxY) throws IllegalStateException {
        Map<Position, Cell> board = new HashMap<>();
        Position startPosition = null;
        String[] sections = coordinateString.split(";");
        Map<String, String> sectionMap = new HashMap<>();
        for (String section : sections) {
            if (section.isEmpty()) continue;
            int openParen = section.indexOf('(');
            if (openParen > 0) {
                String type = section.substring(0, openParen).trim();
                String coords = section.substring(openParen);
                sectionMap.put(type, coords);
            }
        }
        Position startCoord = extractSingleCoordinate(sectionMap.getOrDefault("Start", ""));
        if (startCoord != null) {
            board.put(startCoord, new Cell(CellType.START));
            startPosition = startCoord;
        }
        Position goalCoord = extractSingleCoordinate(sectionMap.getOrDefault("Goal", ""));
        if (goalCoord != null) {
            board.put(goalCoord, new Cell(CellType.GOAL));
        }
        buildPositionCell(sectionMap, "Keys", board, CellType.KEY);
        buildPositionCell(sectionMap, "Locks", board, CellType.LOCK);
        buildPositionCell(sectionMap, "Floors", board, CellType.FLOOR);
        buildPositionCell(sectionMap, "DoublePasses", board, CellType.DOUBLE_PASS);
        Map<Position, Color> cellColors = new HashMap<>();
        applyColors(cellColors, sectionMap);
        return new State(board, startPosition,cellColors);
    }
    private static void buildPositionCell(Map<String, String> sectionMap, String Locks, Map<Position, Cell> board, CellType lock) {
        List<Position> coordinates = extractMultipleCoordinates(sectionMap.getOrDefault(Locks, ""));
        for (Position pos : coordinates) {
            board.put(pos, new Cell(lock));
        }
    }

    private static Position extractSingleCoordinate(String coordString) {
        Pattern pattern = Pattern.compile("\\((\\d+),(\\d+)\\)");
        Matcher matcher = pattern.matcher(coordString);
        if (matcher.find()) {
            try {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                return new Position(x, y);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    private static void applyColors(Map<Position, Color> cellColors, Map<String, String> sectionMap) {

        String colorsString = sectionMap.getOrDefault("Colors", "");

        Pattern pattern = Pattern.compile("\\((\\d+),(\\d+)\\):([A-Z_]+)");
        Matcher matcher = pattern.matcher(colorsString);

        while (matcher.find()) {
            try {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                String colorName = matcher.group(3).toUpperCase();
                Position pos = new Position(x, y);
                Color color = Color.valueOf(colorName);

                cellColors.put(pos, color);
            } catch (IllegalArgumentException e) {
                System.err.println("Error parsing color entry: " + matcher.group(0) + ". Skipping.");
            }
        }
    }
    private static List<Position> extractMultipleCoordinates(String coordString) {
        Pattern pattern = Pattern.compile("\\((\\d+),(\\d+)\\)");
        Matcher matcher = pattern.matcher(coordString);
        return matcher.results()
                .map(match -> {
                    try {
                        int x = Integer.parseInt(match.group(1));
                        int y = Integer.parseInt(match.group(2));
                        return new Position(x, y);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}