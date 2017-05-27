package com.dennyac.HbaseTest.mysql;

import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Created by wanghaiwei on 2016/6/17.
 */

public class DeleteForRfm {

    static ConcurrentLinkedQueue<SearchHit> queuesforconverse = new ConcurrentLinkedQueue<>();

    static ConcurrentLinkedQueue<Integer> dp_ids = new ConcurrentLinkedQueue<>();

    static {
        Integer[] dpids = {66674010, 73524352, 35715903, 72233068, 148992930,
                57303548, 114640410, 72002514, 113076124, 69777212, 35072831,
                61312763, 347338782, 68409996, 105105066, 68703542, 110295122,
                70831814, 34146166, 68254146, 72215400, 106540566, 103570921,
                60311130, 71617132, 104957973, 72249430, 1432171, 112020670,
                108881598, 112781746, 62071262, 69276316, 101814254, 69121666,
                67870070, 73390013, 60137993, 106125764, 59083927, 64698836,
                63747604, 72960322, 111890927, 58951665, 107186575, 105531372,
                101737739, 113475517, 68675934, 72648237, 58024626, 36993267,
                116461651, 105013368, 69002509, 35364022, 65219007, 105128673,
                111693816, 70155447, 72878037, 66637392, 72170233, 67723454,
                148026794, 70634472, 104417480, 66488097, 73087648, 107413958,
                110912789, 35105002, 35732921, 106089869, 106140265, 109813691,
                154929642, 128596787, 104892255, 126827321, 100429646, 61631457,
                102180023, 104083279, 57532439, 106495500, 57300442, 64569608,
                33423424, 33414670, 67255339, 150575099, 100242061, 57960024,
                71839064, 66732327, 61735918, 104476434, 33272014, 58325161,
                101735469, 72112440, 102358370, 62692116, 68057798, 108076401,
                62712659, 118897041, 105770210, 108693938, 103728305, 114638444,
                62657850, 33950842, 67575372, 112458334, 60423672, 103042368, 71753975,
                33737602, 117181874, 148132858, 69148248, 103731545, 105417265, 60433360, 67582042, 104109622, 105974101, 102940656, 106975856, 101066653, 143080327, 110204988, 110687344, 72083305, 101683952, 145346309, 73505107, 33732797, 34567734, 73205976, 68238254, 102776170, 36745682, 70444919, 62353490, 100873460, 66269565, 108773768, 62514497, 64458023, 58711910, 35423632, 64557510, 63078560, 67773509, 64959235, 33277164, 108711120, 106541763, 71811242, 100406911, 108509051, 106540552, 104954182, 33953978, 33365489, 106174987, 103404629, 108515032, 103433478, 67840226, 114119246, 33774630, 111002853, 60450926, 62147848, 34749313, 125247002, 67877521, 105280324, 105110058, 36624036, 113445277, 34546792, 103407096, 71642539, 59918895, 126813084, 104987223, 103979875, 107383415, 65018546, 63652919, 100829591, 100360646, 145238183, 67689181, 60720412, 106309814, 111301660, 33644433, 104488880, 72249222, 111702616, 102275901, 103732674, 148443812, 34894422, 62845173, 68411325, 71248040, 72110007, 101881409, 70927011, 114073418, 35029017, 101738002, 70737619, 35773018, 101961076, 63703916, 115350119, 58171853, 33085487, 67872646, 106009887, 63095839, 58292116, 36441480, 114410002, 66829689, 105738131, 60116766, 33552611, 128988061, 106304631, 60432140, 68796628, 33340474, 68362100, 67253023, 34112632, 104290452, 67353765, 34970285, 101303281, 152626143, 71304727, 34282637, 111721748, 73309925, 106135051, 101887492, 59841992, 106502851, 60986721, 61395626, 64527852, 57208118, 65680107, 111571009, 62696811, 65178878, 62540569, 35078370, 108389271, 111062129, 117126745, 33483998, 65256287, 71496731, 111280656, 65453770, 102305990, 63229819, 62405702, 33047493, 59851646, 36751880, 159470879, 73308420, 103399733, 110607988, 69210103, 60536798, 108157572, 34691851, 34628072, 67262629, 106121127, 60484349, 10981905, 63798330, 101052739, 63712960, 143619898, 73188751, 57703074, 59517042, 102159790, 108543015, 100113211, 67939713, 36719801, 108490402, 34612868, 108357058, 106304547, 59636538, 108243594, 70487457, 59636149, 61054824, 35205595, 128105234, 104629622, 101710761, 110872086, 60935420, 110873923, 127380173, 72860771, 58670516, 72407845, 59210642, 64520716, 65114927, 101252193, 61389996, 104478449, 73041670, 103054086, 57300261, 68154910, 100284744, 68076062, 59607371, 109614745, 67857179, 36975666, 66551655, 72573427, 107601625, 113601649, 109590963, 122487896, 35400517, 36859132, 70524593, 72570937, 72679099, 103406066, 34007464, 109147885, 58145225, 114270604, 63247530, 119778691, 66724012, 108661575, 103087782, 71555659, 105731769, 103528434, 36043742, 106332002, 35013692, 72172211, 106503224, 104678784, 127250195, 59307797, 33406244, 103185841, 100548425, 61994660, 70914592, 101557921, 103035091, 106357670, 104262267, 126011863, 116992858, 119866264, 60505371, 104269367, 64661284, 57302398, 59568783, 106765430, 109506356, 37076658, 68889554, 106217487, 62857380, 72113075, 59881321, 159674435, 104618071, 71887299, 62733836, 65183269, 70014995, 67704537, 68828725, 57166666, 64583371, 73289922, 59654784, 119401883, 112283642, 103318989, 71714922, 118023921, 106823679, 107034297, 35232210, 104133146, 68615569, 125993943, 62448126, 69913828, 66185829, 112522443, 130181931, 67772348, 111861233, 68448451, 103364027, 101335191, 113626288, 104177294, 70314030, 163572048, 33081871, 109347059, 60510996, 33682102, 105081632, 123939627, 112521535, 145950217, 69842499, 36094133, 34634405, 72176048, 125698846, 105759043, 57086709, 100479422, 36167833, 63347705, 68470294, 118822803, 59102392, 110053883, 62721646, 35928073, 34930936, 110119854, 69951254, 104647377, 72003169, 113878797, 143225986, 57536089, 35959838, 121295958, 61678650, 67701189, 116686024, 57303409, 112659507, 59788466, 60466538, 100298527, 62486311, 103777482, 69850307, 72227141, 33195934, 68172606, 64744280, 58697989, 71199598, 101608151, 105200655, 110978822, 33910528, 104595255, 66460770, 36427475, 57300897, 103379789, 33047306, 111006005, 105494345, 105548461, 108864962, 65304418, 103988574, 34945473, 70445177, 70935699, 116194267, 33684227, 101488237, 116745811, 68390108, 103528383, 59340774, 62734374, 113340397, 101321152, 66548882, 111986612, 33375831, 101671477, 113809473, 102699238, 66977196, 70883420, 112298802, 104844651, 60675306, 109867263, 61588448, 61374634, 33238153, 165619466, 108162914, 68396778, 100764260, 62391334, 70604260, 110828460, 58700881, 106508068, 60522439, 70413345, 62797966, 106115993, 104599652, 103986147, 67137351, 73289704, 106606448, 62940100, 60902765, 34129301, 62045907, 69486815, 100325487, 105757078, 104306945, 70003372, 63975641, 68977910, 117227299, 73351129, 64051373, 116056194, 60064178, 117723314, 57302952, 100330260, 112480069, 145655639, 67156417, 106510042, 110413021, 118032949, 106084948, 36034614, 35224889, 60155391, 60579565, 116600530, 106566637, 114296057, 115632716, 72444613, 115838581, 72233425, 72324425, 131124918, 101099428, 37012631, 150656019, 35745362, 60299962, 113912337, 35034226, 35026209, 102419010, 62907614, 60790435, 72571431, 64937310, 103354850, 65072766, 71615245, 108767339, 110667155, 103544427, 163381927, 113269733, 100022757, 57788214, 33457647, 68395556, 35257690, 70774632, 73313305, 101852068, 106858014, 113114005, 102922587, 67133263, 109177642, 110053277, 36803829, 106127737, 59369053, 66257383, 137462011, 73384718, 100739744, 70846550, 105794986, 69837397, 112760205, 64450538, 116606947, 70427538, 35347386, 107242937, 33613970, 108499023, 61040011, 57301850, 68470843, 113646317, 34469708, 57301876, 101437296, 60741494, 110546698, 101594932, 103753827, 68999491, 115744128, 65219342, 66049865, 114817313, 106544631, 111207326, 108528915, 69826775, 36448325, 62791271, 57299200, 101382690, 107435776, 100563566, 128175844, 126904559, 68358856, 104184293, 103681418, 116796759, 66255214, 62710333, 71084076, 106023873, 69847267, 60422155, 277524, 34430869, 72561475, 69993820, 63536804, 70375391, 57604395, 110093022, 112674964, 61852263, 72160346, 111968420, 100175676, 35035320, 65453170, 67477545, 33675289, 143142328, 105846364, 103094659, 107577829, 110212629, 72950918, 107098198, 33405476, 103988908, 110422116, 62447711, 62738517, 101976707, 68953874, 61095482, 62651732, 112821236, 155288488, 103992228, 34915821, 64971222, 113102276, 100009239, 57858226, 62182816, 144158447, 34284882, 105147261, 33342659, 69293010, 62072289, 67730224, 71229240, 112899944, 62641564, 100692158, 67805115, 108513695, 58444730, 166108581, 64066427, 107699374, 100004750, 69928562, 101990603, 70241926, 101887120, 59979586, 68993535, 112344001, 119412526, 63795153, 67618255, 33333196, 59927296, 33073092, 145899428, 102167763, 103394389, 101709270, 58027291, 127880290, 67974049, 148655966, 63852613, 117506551, 108441259, 63729187, 62684074, 35650847, 63079408, 61625950, 72069150, 109589466, 111472401, 106375727, 67629669, 105664300, 65707026, 105721129, 101881543, 156486717, 68347090, 113470497, 61133098, 34211392, 106414792, 105372372, 104281340, 108606624, 114662531, 116913844, 69698375, 103962135, 61412736, 62299638, 112318168, 64483295, 72684658, 124475611, 131150026, 112500954, 66842977, 108332704, 123247977, 36513868, 69273298, 114405016, 70102500, 113167630, 57299922, 102614688, 58493476, 73385382, 114138227, 33077064, 112630446, 111537307, 59389483, 60183582, 62046858, 70222844, 60079153, 102334399, 67748774, 113325678, 68491351, 100355582, 60939225, 35970350, 69947729, 62803606, 116753110, 100476506, 113440028, 71472268, 73507129, 105735042, 59335686, 57128774, 126167349, 68821455, 111829076, 111720603, 70215161, 109407961, 73153591, 33432777, 112947506, 68934950, 58848359, 109686418, 143568940, 107328400, 70260641, 103739847, 33503458, 64872336};
        dp_ids.addAll(Arrays.asList(dpids));

    }

    public static void main(String[] agrs) throws Exception{
        Settings settings = Settings.settingsBuilder()
                .put("client.transport.ping_timeout", "20s")
                .put("cluster.name", "elasticsearch-cluster-2.0").build();
        Settings settings1 = Settings.settingsBuilder()
                .put("client.transport.ping_timeout", "20s")
                .put("cluster.name", "elasticsearch-cluster-2.0").build();
        final TransportClient client = TransportClient.builder()
                .settings(settings).build();

        final TransportClient client1 = TransportClient.builder()
                .settings(settings1).build();

        try{
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.26.233.151"), 9500));
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.26.234.6"), 9500));
            client1.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.26.233.233"), 9500));
            client1.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.26.233.243"), 9500));
            client1.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.26.233.237"), 9500));
        }
        catch(UnknownHostException error){

        }

        final long aa = System.currentTimeMillis();

        final ConcurrentHashMap<String, Boolean> hashMap = new ConcurrentHashMap();
        final ConcurrentHashMap<String, Boolean> hashMap2 = new ConcurrentHashMap();

        for(int i = 0; i < 4; i++) {
            new Thread(new Runnable() {
                public void run() {
                    //Add transport addresses and do something with the client...
                    hashMap.put(Thread.currentThread().getName(), Boolean.FALSE);
                    BulkProcessor bulkProcessor = BulkProcessor.builder(
                            client1,
                            new BulkProcessor.Listener() {

                                public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                                    if(bulkResponse.hasFailures()) System.out.print("fail.......");
                                }

                                public void beforeBulk(long executionId,
                                                       BulkRequest request) {
                                }

                                public void afterBulk(long executionId,
                                                      BulkRequest request,
                                                      Throwable failure) {
                                    System.out.print("fail...................");
                                }
                            })
                            .setBulkActions(10000)
                            .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                            .setBackoffPolicy(
                                    BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                            .setConcurrentRequests(1)
                            .build();
                    while (true) {
                        if (!queuesforconverse.isEmpty()) {
                            try {
                                SearchHit searchHit = queuesforconverse.poll();
                                if (searchHit == null) continue;
                                // router
                                bulkProcessor.add(new DeleteRequest("index_rfm_91", "rfm", searchHit.getId()));
                            } catch (Exception e) {
                                System.out.print(e.getMessage());
                            }

                        }
                        if (queuesforconverse.isEmpty() && !hashMap2.values().contains(Boolean.FALSE)) {
                            bulkProcessor.flush();
                            long jjj = System.currentTimeMillis() - aa;
                            System.out.print("   " + Thread.currentThread().getName() + ":" + jjj + "   ");
                            hashMap.put(Thread.currentThread().getName(), Boolean.TRUE);
                            while(hashMap.values().contains(Boolean.FALSE)){
                                try {
                                    Thread.currentThread().sleep(10 * 1000);
                                } catch(Exception e){
                                    e.printStackTrace(System.out);
                                }
                            }
                            bulkProcessor.close();
                            break;
                        }
                    }
                }
            }).start();
        }

        for(int i = 0; i < 4; i++) {
            new Thread(new Runnable() {
                public void run() {
                    hashMap2.put(Thread.currentThread().getName(), Boolean.FALSE);
                    while (true) {
                        if(!dp_ids.isEmpty()){
                            Integer dpid = dp_ids.poll();
                            if(dpid == null){
                                continue;
                            }

                            QueryBuilder queryBuilder = QueryBuilders.termQuery("dp_id", dpid);
                            SearchResponse scrollResp = client.prepareSearch()
                                    .setSearchType(SearchType.QUERY_THEN_FETCH)
                                    .setScroll(new TimeValue(60000))
                                    .setIndices("index_rfm_2")
                                    .setTypes("rfm")
                                    .setSize(5000)
                                    .setQuery(queryBuilder)
                                    .execute().actionGet();

                            long startTime = System.currentTimeMillis();
                            System.out.println(startTime);
                            int ii = 0;
                            int index = 0;
                            long timeforlive = 80000;
                            TimeValue timeValue = new TimeValue(timeforlive);
                            while (true) {
                                queuesforconverse.addAll(Arrays.asList(scrollResp.getHits().getHits()));
                                scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(timeValue).execute().actionGet();
                                System.out.println(Thread.currentThread().getName() + ii + "  " + "use millis" + (startTime - System.currentTimeMillis()));
                                index++;
                                if(index == 2){
                                    timeforlive = 80000;
                                    timeValue = new TimeValue(timeforlive);
                                    int number = queuesforconverse.size();
                                    int sleepTime = 0;
                                    int jj = number/60000;
                                    while(jj > 0){
                                        if(sleepTime > 60 ){
                                            timeValue = new TimeValue(timeforlive + 20000);
                                            break;
                                        }
                                        try{
                                            Thread.sleep(1000);
                                        }
                                        catch (Exception e){

                                        }
                                        sleepTime++;
                                        int number2 = queuesforconverse.size();
                                        jj = number2/60000;
                                    }
                                    index = 0;
                                }
                                if (scrollResp.getHits().getHits().length == 0) {
                                    System.out.println("dp_id" + dpid + "over");
                                    break;
                                }
                                ii++;
                            }
                        }else{
                            hashMap2.put(Thread.currentThread().getName(), Boolean.TRUE);
                        }
                    }
                }
            }).start();
        }
        System.out.println(System.currentTimeMillis());
    }
}

