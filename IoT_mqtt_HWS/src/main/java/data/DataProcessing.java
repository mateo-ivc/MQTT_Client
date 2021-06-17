package data;

import java.util.ArrayList;
import java.util.HashMap;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.json.JSONException;
import org.json.JSONObject;

public class DataProcessing {
	Second time = new Second();
	public HashMap<String, TimeSeriesCollection> collection = new HashMap<String, TimeSeriesCollection>();
	public ArrayList<TimeSeries> series = new ArrayList<TimeSeries>();

	public void addData(JSONObject js, String topic) {
		try {
			int i = 0;
			time = (Second) time.next();
			for (String key : js.keySet()) {
				if (series.size() == i) {
					series.add(new TimeSeries(key));
					series.get(i).setMaximumItemCount(10);
					if (!collection.containsKey(topic)) {
						collection.put(topic, new TimeSeriesCollection());
						collection.get(topic).addSeries(series.get(i));
					} else {
						collection.get(topic).addSeries(series.get(i));
						if (series.size() == 10) {
							series.get(i).delete(0, 2);
						}
					}
				}
				series.get(i).add(time, js.getDouble(key));
				i++;
			}
			// series.clear();
		} catch (JSONException e) {
			System.err.println("DATACOLLECTION: " + e.getMessage());
		}

	}

	public HashMap<String, TimeSeriesCollection> getCollection() {
		return this.collection;

	}

}
