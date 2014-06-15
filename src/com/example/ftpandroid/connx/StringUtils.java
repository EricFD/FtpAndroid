package com.example.ftpandroid.connx;

/**
 * Varaible useful string Utilities
 * @author eric
 *
 */
public class StringUtils {
	final private static int MAX_FIELDS = 100;
	
	private static StringUtils utils = new StringUtils();
	
	/**
	 * Replace alla occurrences from in text with to 
	 * @parem text String to replace substrings in.
	 * @param from String to search for.
	 * @param to String to relace with.
	 * @return String with all occurrences of from substituted with to.
	 */
	
	public static String replaceAll(String text, String from, String to){
		
		StringBuffer result = new StringBuffer();
		int cursor = 0; //Strar at the beginning
		while(true){
			int fromPos = text.indexOf(from, cursor);
			if(fromPos>=0){
				result.append(text.substring(cursor, fromPos));
				result.append(to);
				cursor = fromPos + from.length();
			}else{
				if(cursor<text.length())
					result.append(text.substring(cursor));
				break;
			}
		}
		return result.toString();
	}
	
	/**
	 * Splits string consisting of fields separeted by whitespace into an
	 * array of strings.
	 * @param str string to split
	 * @return array of fields
	 */
	public static String[] split(String str, char token){
		return split(str, utils.new CharSplitter(token));
	}
	
	/**
	 * Splits string consist of fields seprareted by whitespace into array of strings
	 * @param str string to plit
	 * @return array
	 */
	private static String[] split(String str, Splitter splitter){
		String[] fields = new String[MAX_FIELDS];
		int pos = 0;
		StringBuffer field = new StringBuffer();
		for(int i =0; i< str.length(); i++){
			char ch = str.charAt(i);
			if(!splitter.isSeparator(ch))
				field.append(ch);
			else{
				if(field.length()>0){
					fields[pos++] = field.toString();
					field.setLength(0);
				}
			}
		}
		//pick up
		if(field.length()>0){
			fields[pos++] = field.toString();
		}
		String[] result = new String[pos];
		System.arraycopy(fields,  0, result, 0, pos);
		return result;
	}
	
	public interface Splitter{
		boolean isSeparator(char ch);
	}
	
	public class CharSplitter implements Splitter{
		private char token;
		
		CharSplitter(char token){
			this.token = token;
		}
		
		public boolean isSeparator(char ch){
			if(ch == token)
				return true;
			return false;
			
		}
	}
	
	public class WhitespaceSplitter implements Splitter{
		public boolean isSeparator(char ch){
			if(Character.isWhitespace(ch))
				return true;
			return false;
		}
	}
	
	
}
