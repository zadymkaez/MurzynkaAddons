package xyz.nodrop.client.module.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class NumberSetting extends Setting<Double> {
	private final double min;
	private final double max;
	private final double step;

	public NumberSetting(String name, double defaultValue, double min, double max, double step) {
		super(name, defaultValue);
		this.min = min;
		this.max = max;
		this.step = step;
	}

	public double getMin() { return min; }
	public double getMax() { return max; }
	public double getStep() { return step; }
	public int intValue() { return (int) Math.round(value); }
	public float floatValue() { return value.floatValue(); }

	@Override
	public void set(Double v) {
		double snapped = Math.round((v - min) / step) * step + min;
		this.value = Math.max(min, Math.min(max, snapped));
	}

	@Override
	public JsonElement toJson() { return new JsonPrimitive(value); }

	@Override
	public void fromJson(JsonElement json) { set(json.getAsDouble()); }
}
