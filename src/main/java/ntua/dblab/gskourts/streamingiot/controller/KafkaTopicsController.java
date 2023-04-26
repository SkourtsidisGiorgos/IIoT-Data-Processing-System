//package ntua.dblab.gskourts.streamingiot.controller;
//
//import org.apache.kafka.clients.admin.AdminClient;
//import org.apache.kafka.clients.admin.KafkaAdminClient;
//import org.apache.kafka.clients.admin.TopicListing;
//import org.apache.kafka.clients.admin.ListTopicsResult;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaAdmin;
//import org.springframework.kafka.core.KafkaAdminClient;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//
//@Controller
//public class KafkaTopicsController {
//
//    @Autowired
//    private AdminClient adminClient;
//
//    @GetMapping("/kafka-topics")
//    public String kafkaTopics(Model model) throws ExecutionException, InterruptedException {
//        ListTopicsResult listTopicsResult = adminClient.listTopics();
//        Collection<TopicListing> topicListings = listTopicsResult.listings().get();
//        List<TopicModel> topics = new ArrayList<>();
//
//        for (TopicListing topicListing : topicListings) {
//            TopicModel topicModel = new TopicModel(
//                    topicListing.name(),
//                    adminClient.describeTopics(List.of(topicListing.name())).all().get().get(topicListing.name()).partitions().size(),
//                    (short) adminClient.describeTopics(List.of(topicListing.name())).all().get().get(topicListing.name()).partitions().get(0).replicas().size()
//            );
//            topics.add(topicModel);
//        }
//
//        model.addAttribute("topics", topics);
//        return "kafka-topics";
//    }
//
//    public static class TopicModel {
//        private final String name;
//        private final int partitions;
//        private final short replicas;
//
//        public TopicModel(String name, int partitions, short replicas) {
//            this.name = name;
//            this.partitions = partitions;
//            this.replicas = replicas;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public int getPartitions() {
//            return partitions;
//        }
//
//        public short getReplicas() {
//            return replicas;
//        }
//    }
//}
