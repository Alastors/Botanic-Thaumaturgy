package net.minecraft.util;

public class ChatAllowedCharacters
{
    /** Array of the special characters that are allowed in any text drawing of Minecraft. */
    public static final char[] allowedCharacters = new char[] {'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};
    private static final String __OBFID = "CL_00001606";

    public static boolean isAllowedCharacter(char character)
    {
        return character != 167 && character >= 32 && character != 127;
    }

    /**
     * Filter string by only keeping those characters for which isAllowedCharacter() returns true.
     */
    public static String filerAllowedCharacters(String input)
    {
        StringBuilder stringbuilder = new StringBuilder();
        char[] achar = input.toCharArray();
        int i = achar.length;

        for (int j = 0; j < i; ++j)
        {
            char c0 = achar[j];

            if (isAllowedCharacter(c0))
            {
                stringbuilder.append(c0);
            }
        }

        return stringbuilder.toString();
    }
}