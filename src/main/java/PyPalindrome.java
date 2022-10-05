import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalTime.now;

public class PyPalindrome {

    static BigDecimal tamList = new BigDecimal(99_999_999_999_900d);
    static int numberDigitApi = 1000;
    static int numberDigitToTest = 21;
    static BigDecimal index = new BigDecimal(3_834_781_000d);

    public static void main(String[] args) throws IOException, InterruptedException {
        int counter = 1;
        String urlGet = "";
        String pyString = "";
        String evolution = ".";

        System.out.println("Iniciado em: "+ now());
        while (tamList.compareTo(index)>0){
            String txt = "https://api.pi.delivery/v1/pi?start=";
            txt = txt + index + "&numberOfDigits=" + numberDigitApi;
            urlGet = txt;
            try{
                pyString =  pyString + getPyNumberFragment(urlGet);
            }
            catch (Exception e){
                System.out.println("Aguardando 2 minutos");
                Thread.sleep(120000);
                pyString =  pyString + getPyNumberFragment(urlGet);
            }

            if (counter == 65){
                System.out.println(urlGet);
                counter = 1;
                List<String> tempList = getRagePiPalindrome(pyString);
                if (validPyNumberList(tempList)){
                    System.out.println("**********************************************************");
                    System.out.println("**********************************************************");
                    break;
                };
                pyString = "";
            }

            counter ++;
            index = index.add(BigDecimal.valueOf(numberDigitApi));
        }
        System.out.println("Finalizado em: " +now() + " com tantos registros " + index + " com a url: " + urlGet);

    }
    public static String getPyNumberFragment(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

        conn.setRequestMethod("GET");
        //conn.setRequestProperty("Accept", "application/json");
        //conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

        if (conn.getResponseCode() != 200){
            System.out.println("Error " + conn.getResponseCode() + " ao obter dados da URL " + url);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String piGet = br.readLine();
        piGet = piGet.replace("content", "");
        piGet = piGet.replace(":", "");
        piGet = piGet.replace("{\"\"\"", "");
        piGet = piGet.replace("\"}", "");
        conn.disconnect();
        return piGet;
    }

    public static List<String>  getRagePiPalindrome (String strToValidatePi){
        List<String> stringSerparada = new ArrayList<String>();
        int indexVal = 0;

        while (indexVal < strToValidatePi.length()) {
            String temp = strToValidatePi.substring(indexVal, Math.min((indexVal+numberDigitToTest),strToValidatePi.length()));
            if (temp.length()==numberDigitToTest ) {
                stringSerparada.add(temp);
            }
            indexVal++;
        }
        return stringSerparada;
    }

    public static boolean validPyNumberList(List<String> listPyNumber){
        for(int i = 0; i< listPyNumber.size(); i++){
            if (validatePalindromeNumber(listPyNumber.get(i))){
                System.out.println("Numero palíndromo encontrado: " + listPyNumber.get(i));
                if (validatePrimePi(Long.parseLong(listPyNumber.get(i)))){
                    System.out.println("Numero PRIMO palíndromo encontrado: " + listPyNumber.get(i));
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean validatePalindromeNumber (String piNumber){
        String palindromePi = new StringBuffer(piNumber).reverse().toString();
        if (piNumber.equals(palindromePi)){
            return true;
        }
        return false;
    }

    public static boolean validatePrimePi (Long numb){
        for (int z = 2; z < numb; z++){
            if (numb % z ==0){
                return false;
            }
        }
        return true;
    }
}
