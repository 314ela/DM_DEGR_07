package com.upiiz.dm_degr_07.utils;

import com.upiiz.dm_degr_07.R;

import java.text.Normalizer;
import java.util.Locale;

/** Elige la imagen (PNG) del avatar según la raza / tipo de mascota. */
public final class IconoMascota {

    private IconoMascota() { }

    public static int obtener(String raza) {
        String r = normalizar(raza);
        if (contiene(r, "gato", "gata", "michi", "siames", "persa", "angora", "felino", "cat")) {
            return R.drawable.ic_pet_cat;
        }
        if (contiene(r, "conejo", "coneja", "rabbit")) {
            return R.drawable.ic_pet_rabbit;
        }
        if (contiene(r, "hamster", "cuyo", "raton", "huron", "jerbo")) {
            return R.drawable.ic_pet_hamster;
        }
        if (contiene(r, "perico", "loro", "canario", "ave", "pajaro", "cotorra", "periquito", "pollito")) {
            return R.drawable.ic_pet_bird;
        }
        if (contiene(r, "pez", "betta", "goldfish", "pescado", "guppy")) {
            return R.drawable.ic_pet_fish;
        }
        if (contiene(r, "tortuga", "turtle")) {
            return R.drawable.ic_pet_turtle;
        }
        if (r.isEmpty()) {
            return R.drawable.ic_pet_generic;
        }
        // Por defecto: perrito
        return R.drawable.ic_pet_dog;
    }

    private static boolean contiene(String texto, String... palabras) {
        for (String p : palabras) {
            if (texto.contains(p)) return true;
        }
        return false;
    }

    /** Minúsculas y sin acentos: "Hámster" -> "hamster". */
    private static String normalizar(String s) {
        if (s == null) return "";
        String sinAcentos = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sinAcentos.toLowerCase(Locale.ROOT).trim();
    }
}
