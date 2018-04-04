package assfinder.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AssFreqHolder {

    private ListProperty<String> listKeywords = new SimpleListProperty<>();
    private HashMap<String, HashMap<SimpleStringProperty, SimpleIntegerProperty>> assmap;
    private HashMap<SimpleStringProperty, SimpleIntegerProperty> freqmap;

    public  boolean isContainKeyword(String key) {
        if (assmap.containsKey(key)) {
            return true;
        } else {
            return false;
        }
    }

    public ObservableList<Map.Entry<SimpleStringProperty, SimpleIntegerProperty>> getKeywordsProperty() {
        return FXCollections.observableArrayList(freqmap.entrySet());
    }
    public ObservableList<Map.Entry<SimpleStringProperty, SimpleIntegerProperty>> getAssProperty(String key) {
        return FXCollections.observableArrayList(assmap.get(key).entrySet());
    }

    public ObservableList<AssField> getAssField(int minfreq, int mindepth, boolean oft) {
        ObservableList<AssField> list = FXCollections.observableArrayList();
        for (Map.Entry<SimpleStringProperty, SimpleIntegerProperty> entryFreqmap : freqmap.entrySet()) {
            if (entryFreqmap.getValue().get()>=minfreq) {
                for (Map.Entry<SimpleStringProperty, SimpleIntegerProperty> entryAssmap : assmap.get(entryFreqmap.getKey().getValue()).entrySet()) {
                    if (entryAssmap.getValue().get()>=mindepth) {
                        if (oft) {
                            if (entryFreqmap.getValue().get()<entryAssmap.getValue().get()) {
                                list.add(new AssField(entryFreqmap.getKey().get(), entryFreqmap.getValue().get(), entryAssmap.getKey().get(), entryAssmap.getValue().get()));
                            }
                        } else
                            list.add(new AssField(entryFreqmap.getKey().get(), entryFreqmap.getValue().get(), entryAssmap.getKey().get(), entryAssmap.getValue().get()));
                    }
                }
            }
        }
        return list;
    }

    public void loadFromFile(File file) {
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            HashMap<String, Integer> fmap = (HashMap<String, Integer>) ois.readObject();
            HashMap<String, HashMap<String, Integer>> map = (HashMap<String, HashMap<String, Integer>>) ois.readObject();
            ois.close();
            freqmap = createFreqMap(fmap);
            assmap = createAssWeightMapProperty(map);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        listKeywords.set(FXCollections.observableArrayList(assmap.keySet()));
        listKeywords.sort(Comparator.naturalOrder());
    }
    private HashMap<SimpleStringProperty, SimpleIntegerProperty> createFreqMap(HashMap<String, Integer> fmap) {
        HashMap<SimpleStringProperty, SimpleIntegerProperty> map = new HashMap<>();
        for (Map.Entry<String, Integer> entry : fmap.entrySet()) {
            map.put(new SimpleStringProperty(entry.getKey()), new SimpleIntegerProperty(entry.getValue()));
        }
        return map;
    }
    private HashMap<String, HashMap<SimpleStringProperty, SimpleIntegerProperty>> createAssWeightMapProperty(HashMap<String, HashMap<String, Integer>> map) {
        HashMap<String, HashMap<SimpleStringProperty, SimpleIntegerProperty>> p_map = new HashMap<>();
        HashMap<SimpleStringProperty, SimpleIntegerProperty> p_assmap;
        for (Map.Entry<String, HashMap<String, Integer>> entryMap : map.entrySet()) {
            p_assmap = new HashMap<>();
            for (Map.Entry<String, Integer> entryAss : entryMap.getValue().entrySet()) {
                p_assmap.put(new SimpleStringProperty(entryAss.getKey()), new SimpleIntegerProperty(entryAss.getValue()));
            }
            p_map.put(entryMap.getKey(), p_assmap);
        }
        return p_map;
    }
}
