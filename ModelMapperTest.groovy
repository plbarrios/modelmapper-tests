import org.junit.Test
import org.modelmapper.ModelMapper
import org.modelmapper.PropertyMap

class ModelMapperTest extends GroovyTestCase{

    ModelMapper mapper = new ModelMapper()

    static class A {
        String title
        byte[] someBytes
    }

    static class B {
        String title
        byte[] someBytes
    }

    @Test
    public void testOk() {
        A input = new A()
        input.title = "ABCDEF"
        input.someBytes = input.title.getBytes()

        B result = mapper.map(input, B.class)

        assertEquals(input.title, result.title)
        assertTrue(Arrays.equals(input.someBytes, result.someBytes))
    }

    @Test
    public void testSkipping() {
        // fails right here:
        mapper.addMappings(new PropertyMap<A, B>() {
            @Override
            protected void configure() {
                skip().setSomeBytes(null)
                map().setTitle(source.getTitle())
            }
        })

        A input = new A()
        input.title = "ABCDEF"
        input.someBytes = input.title.getBytes()

        B result = mapper.map(input, B.class)

        assertEquals(input.title, result.title)
        assertNull(result.someBytes)
    }

    @Test
    public void testManualMapping() {
        // fails right here:
        mapper.addMappings(new PropertyMap<A, B>() {
            @Override
            protected void configure() {
                map().setSomeBytes(source.getSomeBytes())
                map().setTitle(source.getTitle())
            }
        })

        A input = new A()
        input.title = "ABCDEF"
        input.someBytes = input.title.getBytes()

        B result = mapper.map(input, B.class)

        assertEquals(input.title, result.title)
        assertTrue(Arrays.equals(input.someBytes, result.someBytes))

    }
}
