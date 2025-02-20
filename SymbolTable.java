package CompConstructionPackage;

import java.util.HashMap;
import java.util.Map;



public class SymbolTable {
	private Map<String, String> table;

    public SymbolTable() {
        table = new HashMap<>();
    }

    public void addEntry(String name, String type, boolean isGlobal) {
        if (!table.containsKey(name)) { 
            table.put(name, type);
        }
    }
    public String lookup(String name) {
        return table.getOrDefault(name, "unknown");
    }
    public void displayTable() {
        System.out.println("Symbol Table:");
        for (Map.Entry<String, String> entry : table.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " (Global)"); // Ensure only one (Global)
        }
    }


}
