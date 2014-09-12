package water;

import water.Model.ModelCategory;
import water.api.AUCData;
import water.api.ConfusionMatrix;
import water.fvec.Frame;
import water.util.Log;


/**
 * Container to hold the metric for a model as scored on a specific frame.
 */

public final class ModelMetrics extends Iced {
  // @API(help="The unique ID (key / uuid / creation timestamp) for the model used for this scoring run.", required=false, filter=Default.class, json=true)
  public UniqueId model = null;
  // @API(help="The category (e.g., Clustering) for the model used for this scoring run.", required=false, filter=Default.class, json=true)
  public Model.ModelCategory model_category = null;
  // @API(help="The unique ID (key / uuid / creation timestamp) for the frame used for this scoring run.", required=false, filter=Default.class, json=true)
  public UniqueId frame = null;

  // @API(help="The duration in mS for this scoring run.", required=false, filter=Default.class, json=true)
  public long duration_in_ms =-1L;
  // @API(help="The time in mS since the epoch for the start of this scoring run.", required=false, filter=Default.class, json=true)
  public long scoring_time = -1L;

  // @API(help="The AUC object for this scoring run.", required=false, filter=Default.class, json=true)
  public AUCData auc = null;
  // @API(help="The ConfusionMatrix object for this scoring run.", required=false, filter=Default.class, json=true)
  public ConfusionMatrix cm = null;

  public ModelMetrics(UniqueId model, ModelCategory model_category, UniqueId frame, long duration_in_ms, long scoring_time, AUCData auc, ConfusionMatrix cm) {
    this.model = model;
    this.model_category = model_category;
    this.frame = frame;
    this.duration_in_ms = duration_in_ms;
    this.scoring_time = scoring_time;

    this.auc = auc;
    this.cm = cm;
  }

  public static Key buildKey(Model model, Frame frame) {
    return Key.make("modelmetrics_" + model.getUniqueId().getId() + "_on_" + frame.getUniqueId().getId());
  }


  public static Key buildKey(UniqueId model, UniqueId frame) {
    return Key.make("modelmetrics_" + model.getId() + "_on_" + frame.getId());
  }

  public Key buildKey() {
    return Key.make("modelmetrics_" + this.model.getId() + "_on_" + this.frame.getId());
  }

  public void putInDKV() {
    Key metricsKey = this.buildKey();

    Log.debug("Putting ModelMetrics: " + metricsKey.toString());
    DKV.put(metricsKey, this);
  }

  public static ModelMetrics getFromDKV(Model model, Frame frame) {
    Key metricsKey = buildKey(model, frame);

    Log.debug("Getting ModelMetrics: " + metricsKey.toString());
    Value v = DKV.get(metricsKey);

    if (null == v)
      return null;

    return (ModelMetrics)v.get();
  }

  public static ModelMetrics getFromDKV(UniqueId model, UniqueId frame) {
    Key metricsKey = buildKey(model, frame);
    Log.debug("Getting ModelMetrics: " + metricsKey.toString());
    Value v = DKV.get(metricsKey);

    if (null == v)
      return null;

    return (ModelMetrics)v.get();
  }
}
