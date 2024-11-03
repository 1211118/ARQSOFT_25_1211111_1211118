package pt.psoft.g1.psoftg1.authormanagement.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BioTest {

    @Test
    void ensureBioMustNotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new Bio(null));
    }

    @Test
    void ensureBioMustNotBeBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Bio(""));
    }


    /**
     * Text from <a href="https://www.lipsum.com/">Lorem Ipsum</a> generator.
     */
    @Test
    void ensureBioMustNotBeOversize() {
        assertThrows(IllegalArgumentException.class, () -> new Bio("\n" +
                "\n" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam venenatis semper nisl, eget condimentum felis tempus vitae. Morbi tempus turpis a felis luctus, ut feugiat tortor mattis. Duis gravida nunc sed augue ultricies tempor. Phasellus ultrices in dolor id viverra. Sed vitae odio ut est vestibulum lacinia sed sed neque. Mauris commodo, leo in tincidunt porta, justo mi commodo arcu, non ultricies ipsum dolor a mauris. Pellentesque convallis vulputate nisl, vel commodo felis ornare nec. Aliquam tristique diam dignissim hendrerit auctor. Mauris nec dolor hendrerit, dignissim urna non, pharetra quam. Sed diam est, convallis nec efficitur eu, sollicitudin ac nibh. In orci leo, dapibus ut eleifend et, suscipit sit amet felis. Integer lectus quam, tristique posuere vulputate sed, tristique eget sem.\n" +
                "\n" +
                "Mauris ac neque porttitor, faucibus velit vel, congue augue. Vestibulum porttitor ipsum eu sem facilisis sagittis. Mauris dapibus tincidunt elit. Phasellus porttitor massa nulla, quis dictum lorem aliquet in. Integer sed turpis in mauris auctor viverra. Suspendisse faucibus tempus tellus, in faucibus urna dapibus at. Nullam dolor quam, molestie nec efficitur nec, bibendum a nunc.\n" +
                "\n" +
                "Maecenas quam arcu, euismod sit amet congue non, venenatis nec ipsum. Cras at posuere metus. Quisque facilisis, sem sit amet vestibulum porta, augue quam semper nulla, eu auctor orci purus vel felis. Fusce ultricies tristique tellus, sed rhoncus elit venenatis id. Aenean in lacus quis ipsum eleifend viverra at at lacus. Nulla finibus, risus ut venenatis posuere, lacus magna eleifend arcu, ut bibendum magna turpis eu lorem. Mauris sed quam eget libero vulputate pretium in in purus. Morbi nec faucibus mi, sit amet pretium tellus. Duis suscipit, tellus id fermentum ultricies, tellus elit malesuada odio, vitae tempor dui purus at ligula. Nam turpis leo, dignissim tristique mauris at, rutrum scelerisque est. Curabitur sed odio sit amet nisi molestie accumsan. Ut vulputate auctor tortor vel ultrices. Nam ut volutpat orci. Etiam faucibus aliquam iaculis.\n" +
                "\n" +
                "Mauris malesuada rhoncus ex nec consequat. Etiam non molestie libero. Phasellus rutrum elementum malesuada. Pellentesque et quam id metus iaculis hendrerit. Fusce molestie commodo tortor ac varius. Etiam ac justo ut lacus semper pretium. Curabitur felis mauris, malesuada accumsan pellentesque vitae, posuere non lacus. Donec sit amet dui finibus, dapibus quam quis, tristique massa. Phasellus velit ipsum, facilisis vel nisi eu, interdum vehicula ante. Nulla eget luctus nunc, nec ullamcorper lectus.\n" +
                "\n" +
                "Curabitur et nisi nisi. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Curabitur ultrices ultrices ante eu vestibulum. Phasellus imperdiet non ex sed rutrum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Donec consequat mauris sed pulvinar sodales. Quisque a iaculis eros. Donec non tellus eget ligula eleifend posuere. Sed tincidunt, purus id eleifend fringilla, tellus erat tristique urna, at ullamcorper purus turpis ac risus. Maecenas non finibus diam. Aliquam erat volutpat. Morbi ultrices blandit arcu eu dignissim. Duis ac dapibus libero. Ut pretium libero sit amet velit viverra semper. Suspendisse vitae augue dui.\n" +
                "\n" +
                "Aliquam aliquam justo porttitor sapien faucibus sollicitudin. Sed iaculis accumsan urna, id elementum est rhoncus vitae. Maecenas rhoncus ultrices arcu eu semper. Integer pulvinar ultricies purus, sit amet scelerisque dui vehicula vel. Phasellus quis urna ac neque auctor scelerisque eget eget arcu. Sed convallis, neque consectetur venenatis ornare, nibh lorem mollis magna, vel vulputate libero ligula egestas ligula. Curabitur iaculis nisl nisi, ac ornare urna lacinia non. Cras sagittis risus sit amet interdum porta. Nam dictum, neque ut blandit feugiat, tortor libero hendrerit enim, at tempor justo velit scelerisque odio. Fusce a ipsum sit amet ligula maximus pharetra. Suspendisse rhoncus leo dolor, vulputate blandit mi ullamcorper ut. Etiam consequat non mi eu porta. Sed mattis metus fringilla purus auctor aliquam.\n" +
                "\n" +
                "Vestibulum quis mi at lorem laoreet bibendum eu porta magna. Etiam vitae metus a sapien sagittis dapibus et et ex. Vivamus sed vestibulum nibh. Etiam euismod odio massa, ac feugiat urna congue ac. Phasellus leo quam, lacinia at elementum vitae, viverra quis ligula. Quisque ultricies tellus nunc, id ultrices risus accumsan in. Vestibulum orci magna, mollis et vehicula non, bibendum et magna. Pellentesque ut nibh quis risus dignissim lacinia sed non elit. Morbi eleifend ipsum posuere velit sollicitudin, quis auctor urna ullamcorper. Praesent pellentesque non lacus eu scelerisque. Praesent quis eros sed orci tincidunt maximus. Quisque imperdiet interdum massa a luctus. Phasellus eget nisi leo.\n" +
                "\n" +
                "Nunc porta nisi eu dui maximus hendrerit eu quis est. Cras molestie lacus placerat, maximus libero hendrerit, eleifend nisi. Suspendisse potenti. Praesent nec mi ut turpis pharetra pharetra. Phasellus pharetra. "));
    }

    @Test
    void ensureBioIsSet() {
        final var bio = new Bio("Some bio");
        assertEquals("Some bio", bio.toString());
    }

    @Test
    void ensureBioIsChanged() {
        final var bio = new Bio("Some bio");
        bio.setBio("Some other bio");
        assertEquals("Some other bio", bio.toString());
    }

    @Test
    void ensureBioWithMaxLengthIsAccepted() {
        // Teste para garantir que uma Bio com exatamente 4096 caracteres é aceita
        StringBuilder maxLengthBio = new StringBuilder();
        for (int i = 0; i < 4096; i++) {
            maxLengthBio.append("a");
        }
        Bio bio = new Bio(maxLengthBio.toString());
        assertNotNull(bio);
        assertEquals(maxLengthBio.toString(), bio.toString());
    }

    @Test
    void ensureBioIsSanitized() {
        // Teste para garantir que HTML perigoso é removido
        String bioContent = "<script>alert('attack')</script> Clean content";
        Bio bio = new Bio(bioContent);
        // Pressupondo que StringUtilsCustom.sanitizeHtml remove a tag <script>
        assertEquals(" Clean content", bio.toString(), "Bio content should be sanitized from dangerous HTML.");
    }

    @Test
    void ensureValidBioIsSet() {
        // Teste para garantir que uma Bio válida é configurada corretamente
        String validBioContent = "This is a valid bio content.";
        Bio bio = new Bio(validBioContent);
        assertEquals(validBioContent, bio.toString(), "Bio content should match the input content.");
    }

    @Test
    void ensureBioCanBeUpdatedWithValidContent() {
        // Teste para garantir que é possível atualizar a Bio com um conteúdo válido
        Bio bio = new Bio("Initial bio content.");
        bio.setBio("Updated bio content.");
        assertEquals("Updated bio content.", bio.toString(), "Bio content should be updated correctly.");
    }

    @Test
    void ensureUpdatingBioToNullThrowsException() {
        // Teste para garantir que atualizar a Bio para nulo gera uma exceção
        Bio bio = new Bio("Initial bio content.");
        assertThrows(IllegalArgumentException.class, () -> bio.setBio(null), "Bio cannot be null");
    }

    @Test
    void ensureUpdatingBioToBlankThrowsException() {
        // Teste para garantir que atualizar a Bio para uma string em branco gera uma exceção
        Bio bio = new Bio("Initial bio content.");
        assertThrows(IllegalArgumentException.class, () -> bio.setBio(" "), "Bio cannot be blank");
    }

    @Test
    void ensureUpdatingBioWithExceedingLengthThrowsException() {
        // Teste para garantir que uma atualização da Bio com mais de 4096 caracteres gera uma exceção
        Bio bio = new Bio("Initial bio content.");
        StringBuilder exceedingBioContent = new StringBuilder();
        for (int i = 0; i <= 4096; i++) {
            exceedingBioContent.append("a");
        }
        assertThrows(IllegalArgumentException.class, () -> bio.setBio(exceedingBioContent.toString()), "Bio has a maximum of 4096 characters");
    }
}
