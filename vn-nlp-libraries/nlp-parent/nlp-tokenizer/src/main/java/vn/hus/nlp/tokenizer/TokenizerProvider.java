/**
 * (C) Le Hong Phuong, phuonglh@gmail.com
 * vn.hus.tokenizer
 * 2007
 */
package vn.hus.nlp.tokenizer;

import vn.hus.nlp.tokenizer.segmenter.AbstractResolver;
import vn.hus.nlp.tokenizer.segmenter.Segmenter;
import vn.hus.nlp.tokenizer.segmenter.UnigramResolver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


/**
 * @author LE Hong Phuong
 *         <p>
 *         8 janv. 07
 *         </p>
 *         A provider of tokenizer. It creates a tokenizer for Vietnamese.
 */
public final class TokenizerProvider {

    /**
     * A lexical segmenter
     */
    private Segmenter segmenter;
    /**
     * An ambiguity resolver
     */
    private AbstractResolver resolver;
    /**
     * The tokenizer
     */
    private Tokenizer tokenizer;
    /**
     * An instance flag
     */
    private static boolean instanceFlag = false;

    private static TokenizerProvider provider;

    /**
     * Private constructor
     */
    private TokenizerProvider() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/tokenizer.properties"));
            // create a unigram resolver.
            resolver = new UnigramResolver(properties.getProperty("unigramModel"));
            // create a lexical segmenter that use the unigram resolver
            segmenter = new Segmenter(properties, resolver);
            // init the tokenizer
            tokenizer = new Tokenizer(properties, segmenter);
            // Do not resolve the ambiguity.
//			tokenizer.setAmbiguitiesResolved(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TokenizerProvider(String propertiesFilename) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(propertiesFilename));
            // create a unigram resolver.
            //
            resolver = new UnigramResolver(properties.getProperty("unigramModel"));
            // create a lexical segmenter that use the unigram resolver
            segmenter = new Segmenter(properties, resolver);
            // init the tokenizer
            tokenizer = new Tokenizer(properties, segmenter);
            // Do not resolve the ambiguity.
//			tokenizer.setAmbiguitiesResolved(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private TokenizerProvider(Properties properties) {
        // create a unigram resolver.
        resolver = new UnigramResolver(properties.getProperty("unigramModel"));
        // create a lexical segmenter that use the unigram resolver
        segmenter = new Segmenter(properties, resolver);
        // init the tokenizer
        tokenizer = new Tokenizer(properties, segmenter);
        // Do not resolve the ambiguity.
//		tokenizer.setAmbiguitiesResolved(false);
    }

    /**
     * Instantiate a tokenizer provider object.
     *
     * @return a provider object
     */
    public static TokenizerProvider getInstance() {
        if (!instanceFlag) {
            instanceFlag = true;
            provider = new TokenizerProvider();
        }
        return provider;
    }

    /**
     * Instantiate a tokenizer provider object, parameters are read from a properties file.
     *
     * @return a provider object
     */
    public static TokenizerProvider getInstance(String propertiesFilename) {
        if (!instanceFlag) {
            instanceFlag = true;
            provider = new TokenizerProvider(propertiesFilename);
        }
        return provider;
    }

    /**
     * Instantiate a tokenizer provider object, parameters are read from a properties object.
     *
     * @return a provider object
     */
    public static TokenizerProvider getInstance(Properties properties) {
        if (!instanceFlag) {
            instanceFlag = true;
            provider = new TokenizerProvider(properties);
        }
        return provider;
    }

    /**
     * Get the lexical segmenter
     *
     * @return the lexical segmenter
     */
    public Segmenter getSegmenter() {
        return segmenter;
    }

    /**
     * Returns the tokenizer
     *
     * @return
     */
    public Tokenizer getTokenizer() {
        return tokenizer;
    }

    /**
     * Dispose the data provider
     */
    public void dispose() {
        // dispose the tokenizer
        // this will dispose the lexical tokenizer and the automata
        tokenizer.dispose();
    }
}
