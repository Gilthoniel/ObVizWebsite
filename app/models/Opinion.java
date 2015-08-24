package models;

import java.io.Serializable;
import java.util.*;

/**
 * Created by gaylor on 02.07.15.
 * Opinion in a clause
 */
public class Opinion implements Serializable {

    private static final long serialVersionUID = -4273595504109641382L;

    enum Type { CLASSIC, WEAK }

    public int topicID;
    public int nbOpinions;
    public int opinionValue;
    public List<OpinionDetail> opinions;

    public class OpinionDetail implements Serializable {

        private static final long serialVersionUID = 7726615802718595835L;
        public String polarity;
        public boolean isNegated;
        public boolean isInTitle;
        public Boolean isGoodOpinion;
        public Type type;
        List<String> polarityWords;
        List<String> aspects;
        List<String> otherWords;
        int sentenceID;
        int clauseID;
        String phrase;

        public List<String> getWords() {
            List<String> words = new ArrayList<>();
            words.addAll(polarityWords);

            if (aspects != null) {
                words.addAll(aspects);
            }

            if (otherWords != null) {
                words.addAll(otherWords);
            }

            return words;
        }
    }

    public static class ParsedOpinion extends HashMap<Integer, Map<Integer, List<OpinionDetail>>> {

        private static final long serialVersionUID = 2089526101092532249L;

        public void put(Opinion opinions, boolean isInTitle) {
            for (OpinionDetail opinion : opinions.opinions) {

                if (isInTitle == opinion.isInTitle) {

                    if (!containsKey(opinion.sentenceID)) {
                        put(opinion.sentenceID, new HashMap<>());
                    }

                    Map<Integer, List<OpinionDetail>> details = get(opinion.sentenceID);

                    int clauseID = opinion.clauseID > 0 ? opinion.clauseID : 0;
                    if (!details.containsKey(clauseID)) {
                        details.put(clauseID, new ArrayList<>());
                    }

                    List<OpinionDetail> detail = details.get(clauseID);
                    detail.add(opinion);
                }
            }
        }

        public List<OpinionDetail> get(int sentenceID, int clauseID) {
            if (containsKey(sentenceID)) {

                Map<Integer, List<OpinionDetail>> details = get(sentenceID);
                if (details.containsKey(clauseID)) {

                    return details.get(clauseID);
                }
            }

            return Collections.emptyList();
        }
    }
}
