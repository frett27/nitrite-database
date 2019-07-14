/*
 *
 * Copyright 2017-2018 Nitrite author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.dizitart.no2.index.fulltext.languages;

import org.dizitart.no2.index.fulltext.Language;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Catalan stop words
 *
 * @since 2.1.0
 * @author Anindya Chatterjee
 */
public class Catalan implements Language {
    @Override
    public Set<String> stopWords() {
        return new HashSet<>(Arrays.asList(
                "a",
                "abans",
                "ací",
                "ah",
                "així",
                "això",
                "al",
                "aleshores",
                "algun",
                "alguna",
                "algunes",
                "alguns",
                "alhora",
                "allà",
                "allí",
                "allò",
                "als",
                "altra",
                "altre",
                "altres",
                "amb",
                "ambdues",
                "ambdós",
                "anar",
                "ans",
                "apa",
                "aquell",
                "aquella",
                "aquelles",
                "aquells",
                "aquest",
                "aquesta",
                "aquestes",
                "aquests",
                "aquí",
                "baix",
                "bastant",
                "bé",
                "cada",
                "cadascuna",
                "cadascunes",
                "cadascuns",
                "cadascú",
                "com",
                "consegueixo",
                "conseguim",
                "conseguir",
                "consigueix",
                "consigueixen",
                "consigueixes",
                "contra",
                "d'un",
                "d'una",
                "d'unes",
                "d'uns",
                "dalt",
                "de",
                "del",
                "dels",
                "des",
                "des de",
                "després",
                "dins",
                "dintre",
                "donat",
                "doncs",
                "durant",
                "e",
                "eh",
                "el",
                "elles",
                "ells",
                "els",
                "em",
                "en",
                "encara",
                "ens",
                "entre",
                "era",
                "erem",
                "eren",
                "eres",
                "es",
                "esta",
                "estan",
                "estat",
                "estava",
                "estaven",
                "estem",
                "esteu",
                "estic",
                "està",
                "estàvem",
                "estàveu",
                "et",
                "etc",
                "ets",
                "fa",
                "faig",
                "fan",
                "fas",
                "fem",
                "fer",
                "feu",
                "fi",
                "fins",
                "fora",
                "gairebé",
                "ha",
                "han",
                "has",
                "haver",
                "havia",
                "he",
                "hem",
                "heu",
                "hi",
                "ho",
                "i",
                "igual",
                "iguals",
                "inclòs",
                "ja",
                "jo",
                "l'hi",
                "la",
                "les",
                "li",
                "li'n",
                "llarg",
                "llavors",
                "m'he",
                "ma",
                "mal",
                "malgrat",
                "mateix",
                "mateixa",
                "mateixes",
                "mateixos",
                "me",
                "mentre",
                "meu",
                "meus",
                "meva",
                "meves",
                "mode",
                "molt",
                "molta",
                "moltes",
                "molts",
                "mon",
                "mons",
                "més",
                "n'he",
                "n'hi",
                "ne",
                "ni",
                "no",
                "nogensmenys",
                "només",
                "nosaltres",
                "nostra",
                "nostre",
                "nostres",
                "o",
                "oh",
                "oi",
                "on",
                "pas",
                "pel",
                "pels",
                "per",
                "per que",
                "perquè",
                "però",
                "poc",
                "poca",
                "pocs",
                "podem",
                "poden",
                "poder",
                "podeu",
                "poques",
                "potser",
                "primer",
                "propi",
                "puc",
                "qual",
                "quals",
                "quan",
                "quant",
                "que",
                "quelcom",
                "qui",
                "quin",
                "quina",
                "quines",
                "quins",
                "què",
                "s'ha",
                "s'han",
                "sa",
                "sabem",
                "saben",
                "saber",
                "sabeu",
                "sap",
                "saps",
                "semblant",
                "semblants",
                "sense",
                "ser",
                "ses",
                "seu",
                "seus",
                "seva",
                "seves",
                "si",
                "sobre",
                "sobretot",
                "soc",
                "solament",
                "sols",
                "som",
                "son",
                "sons",
                "sota",
                "sou",
                "sóc",
                "són",
                "t'ha",
                "t'han",
                "t'he",
                "ta",
                "tal",
                "també",
                "tampoc",
                "tan",
                "tant",
                "tanta",
                "tantes",
                "te",
                "tene",
                "tenim",
                "tenir",
                "teniu",
                "teu",
                "teus",
                "teva",
                "teves",
                "tinc",
                "ton",
                "tons",
                "tot",
                "tota",
                "totes",
                "tots",
                "un",
                "una",
                "unes",
                "uns",
                "us",
                "va",
                "vaig",
                "vam",
                "van",
                "vas",
                "veu",
                "vosaltres",
                "vostra",
                "vostre",
                "vostres",
                "érem",
                "éreu",
                "és",
                "éssent",
                "últim",
                "ús"
        ));
    }
}