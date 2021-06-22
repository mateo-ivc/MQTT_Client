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
					// create new Timeseries
					series.add(new TimeSeries(key));
					// set max items to 10 so only 10 cords are shown in in the chart
					series.get(i).setMaximumItemCount(10);
					if (!collection.containsKey(topic)) {
						// create topic if it doesn't exist
						collection.put(topic, new TimeSeriesCollection());
						// add series to series collection
						collection.get(topic).addSeries(series.get(i));
					} else {
						// add series to series collection if topic exist
						collection.get(topic).addSeries(series.get(i));
					}
				}
				// add data to arraylist
				series.get(i).add(time, js.getDouble(key));
				i++;
			}
		} catch (JSONException e) {
			System.err.println("DATACOLLECTION: " + e.getMessage());
		}

	}

	public HashMap<String, TimeSeriesCollection> getCollection() {
		return this.collection;

	}

}
