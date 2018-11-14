/**
 * Copyright 2004-present, Facebook, Inc.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.profilo.provider.mappingdensity;

import android.util.Log;
import com.facebook.profilo.core.BaseTraceProvider;
import com.facebook.profilo.core.ProvidersRegistry;
import com.facebook.profilo.ipc.TraceContext;
import com.facebook.proguard.annotations.DoNotStrip;
import java.io.File;

@DoNotStrip
public final class MappingDensityProvider extends BaseTraceProvider {

  public static final int PROVIDER_MAPPINGDENSITY =
      ProvidersRegistry.newProvider("mapping_density");

  private boolean mEnabled;

  public MappingDensityProvider() {
    super("profilo_mappingdensity");
  }

  @Override
  protected void enable() {
    mEnabled = true;
  }

  @Override
  protected void disable() {
    mEnabled = false;
  }

  @Override
  protected void onTraceStarted(TraceContext context, File extraDataFolder) {
    doDump(extraDataFolder, "start");
  }

  @Override
  protected void onTraceEnded(TraceContext context, File extraDataFolder) {
    doDump(extraDataFolder, "end");
  }

  @Override
  protected int getSupportedProviders() {
    return PROVIDER_MAPPINGDENSITY;
  }

  private static void doDump(File extraDataFolder, String dumpName) {
    extraDataFolder.mkdirs();
    if (extraDataFolder.exists()) {
      dumpMappingDensities("^/data/(data|app)", extraDataFolder.getPath(), dumpName);
    } else {
      Log.w("Profilo/MappingDensity", "nonexistent extras directory: " + extraDataFolder.getPath());
    }
  }

  private static native void dumpMappingDensities(
      String mapRegex, String extraDataFolderPath, String dumpName);

  @Override
  protected int getTracingProviders() {
    return mEnabled ? PROVIDER_MAPPINGDENSITY : 0;
  }
}
