<template>
</template>

<script setup lang="ts">

import { onMounted, defineProps } from 'vue';
import { TelemetryPageTMSGeneric } from '@/analyticsDataLayer/pages/pages';
import { attachDataLayerObjects, DataLayerGTM, DataLayerTealium, Telemetry } from '@/analyticsDataLayer/analyticsDataLayer';
import { ITelemetryDataLayer, DataLayerTMSGeneric, ITelemetryPage, TelemetryPageGTM, TelemetryPageTealium } from '@/analyticsDataLayer/analyticsDataLayer';

const props = defineProps<{
  pageName: string;
  pageId: string;
}>();

  onMounted(async () => {
      try {
        console.log('HomeView mounted');

        let telemetry: Telemetry = new Telemetry("telemetry-id");
        let dlGeneric: ITelemetryDataLayer = new DataLayerTMSGeneric("Generic");
        let dlGTM: ITelemetryDataLayer = new DataLayerGTM("GTM");
        let dlTealium: ITelemetryDataLayer = new DataLayerTealium("Tealium");

        let pageName = props.pageName;
        let pageId = props.pageId;

        let pageTMSGeneric: ITelemetryPage = new TelemetryPageTMSGeneric(pageName, pageId, true);
        let pageGTM: ITelemetryPage = new TelemetryPageGTM(pageName, pageId, true);
        let pageTealium: ITelemetryPage = new TelemetryPageTealium(pageName, pageId, true);

        dlGeneric.setPage(pageTMSGeneric);
        dlGTM.setPage(pageGTM);
        dlTealium.setPage(pageTealium);

        telemetry.addDataLayer(dlGeneric);
        telemetry.addDataLayer(dlGTM);
        telemetry.addDataLayer(dlTealium);

        console.log('[Analytics] Telemetry page created:', telemetry);

        attachDataLayerObjects(telemetry);

      } catch (err) {
        console.error('Failed to fetch data:', err);
      }
  });

</script>

<style scoped>
/* view-specific styles */
</style>
