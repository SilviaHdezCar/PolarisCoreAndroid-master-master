package com.example.wposs_user.polariscoreandroid.Comun;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Yeison Sanchez on 31/01/2019.
 */

public class ISOUtil {

    public ISOUtil() {

    }

    /*******************************************************************************
     Metodo          : getTime
     Description     : Obtiene el tiempo en formato Horas/minutos/segundos
     Return          : Cadena de tiempo
     ******************************************************************************/
    public static byte[] getTime() {

        String hour, minute, second;
        TimeZone tz = TimeZone.getTimeZone("GMT-5");
        Calendar actualDateTime = Calendar.getInstance(tz);

        hour = String.valueOf(actualDateTime.get(actualDateTime.HOUR_OF_DAY));
        minute = String.valueOf(actualDateTime.get(actualDateTime.MINUTE));
        second = String.valueOf(actualDateTime.get(actualDateTime.SECOND));

        if (actualDateTime.get(actualDateTime.HOUR_OF_DAY) < 10)
            hour = "0" + hour;
        if (actualDateTime.get(actualDateTime.MINUTE) < 10)
            minute = "0" + minute;
        if (actualDateTime.get(actualDateTime.SECOND) < 10)
            second = "0" + second;

        String time = hour + minute + second;
        return ISOUtil.str2bcd(time, true);

    }

    /**
     * Obiene la fecha en formato ISO8583
     *
     * @retorna un array de 2 Bytes en BCD con el formato de MMDD
     */
    public static byte[] getDate() {
        String day, month;
        TimeZone tz = TimeZone.getTimeZone("GMT-5");
        Calendar actualDateTime = Calendar.getInstance(tz);

        month = String.valueOf(actualDateTime.get(actualDateTime.MONTH) + 1);
        day = String.valueOf(actualDateTime.get(actualDateTime.DAY_OF_MONTH));

        if ((actualDateTime.get(actualDateTime.MONTH) + 1) < 10)
            month = "0" + month;
        if (actualDateTime.get(actualDateTime.DAY_OF_MONTH) < 10)
            day = "0" + day;

        String date = month + day;
        return ISOUtil.str2bcd(date, true);
    }
    /**
     * converts to BCD
     *
     * @param s       - the number
     * @param padLeft - flag indicating left/right padding
     * @param d       The byte array to copy into.
     * @param offset  Where to start copying into.
     * @return BCD representation of the number
     */
    public static byte[] str2bcd(String s, boolean padLeft, byte[] d, int offset) {
        int len = s.length();
        int start = (((len & 1) == 1) && padLeft) ? 1 : 0;
        for (int i = start; i < len + start; i++)
            d[offset + (i >> 1)] |= (s.charAt(i - start) - '0') << ((i & 1) == 1 ? 0 : 4);
        return d;
    }

    /**
     * converts to BCD
     *
     * @param s       - the number
     * @param padLeft - flag indicating left/right padding
     * @return BCD representation of the number
     */
    public static byte[] str2bcd(String s, boolean padLeft) {
        int len = s.length();
        byte[] d = new byte[(len + 1) >> 1];
        return str2bcd(s, padLeft, d, 0);
    }

    /**
     * convierte una representacion BCD de un numero a una cadena
     *
     * @param source - representacion BCD
     * @param offset - desplazamiento inicial
     * @param length - tama�o de los Bytes a convertir
     * @retorna una cadena con la representacion del numero
     */
    public static String bcd2str(byte[] source, int offset, int length) {
        char[] ret = new char[length * 2];
        byte car;

        int counter;
        int indexString = 0;

        for (counter = offset; counter < length + offset; counter++) {
            car = (byte) ((source[counter] & 0xF0) >> 4);
            ret[indexString] = (char) (car + ((car < 10) ? '0' : '7'));
            indexString++;
            car = (byte) (source[counter] & 0x0F);
            ret[indexString] = (char) (car + ((car < 10) ? '0' : '7'));
            indexString++;
        }

        return new String(ret);
    }

    /**
     * pad to the left
     *
     * @param s   - original string
     * @param len - desired len
     * @param c   - padding char
     * @return padded string
     */
    public static String padleft(String s, int len, char c) {
        s = s.trim();

        if (s.length() <= len) {
            StringBuffer d = new StringBuffer(len);
            int fill = len - s.length();
            while (fill-- > 0)
                d.append(c);
            d.append(s);
            return d.toString();
        } else
            return s;

    }

    /*******************************************************************************
     Metodo          : padRight
     Description     : Pad a la derecha
     Input           : s = Cadena original
                       len = longitud
                       c = caracter a reemplazar
     Return          : Cadena del pad
     ******************************************************************************/
    public static String padRight(String s, int len, char c) {
        return String.format("%-" + len + "s", s).replace(' ', c);
    }


    /**
     * convierte una Byte a una cadena AscciHexa de 2 digitos
     *
     * @param b: Array de Bytes
     * @return una cadena con la representacion AscciHexa del buffer
     */
    public static String hexString(byte b) {
        String AsciiHexa;
        int c = (int) b;

        if (c < 0)
            c = c - 0xFFFFFF00;

        AsciiHexa = ISOUtil.padleft(Integer.toHexString(c), 2, '0').toUpperCase();

        return AsciiHexa;
    }

    /**
     * convierte una Byte a una cadena AscciHexa de 2 digitos
     *
     * @param : Array de Bytes
     * @return una cadena con la representacion AscciHexa del buffer
     */
    public static boolean filedOn(byte[] bitMap, int field) {
        int byteOn = 0;
        char caracter;
        String binary = null, strBitMap = "";

        for (int i = 0; i < 8; i++) {
            //byteOn = Math.abs(bitMap[i]);
            byteOn = bitMap[i];
            if(byteOn<0)
                byteOn = 256 + byteOn;

            binary = ISOUtil.padleft(Integer.toBinaryString(byteOn), 8, '0');
            strBitMap = strBitMap + binary;
        }

        caracter = strBitMap.charAt(field-1);
        if (caracter == '1')
            return true;
        else
            return false;

    }

    /*******************************************************************************
     Metodo          : stringToHex
     Description     :
     Input           : hex =
     Return          : output
     ******************************************************************************/
    public static StringBuilder stringToHex(String hex)
    {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i+=2) {
            String str = hex.substring(i, i+2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output;
    }


}

