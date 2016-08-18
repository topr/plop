import groovy.util.logging.Slf4j
import plop.server.Plop

@Slf4j(category = 'plop.server.BootStrap')
class BootStrap {

    def toPlop = ['YOLO!', 'ELO!', 'WOOT!']

    def init = { servletContext ->
        log.info('Plop server initialisation...')
        toPlop.each(this.&saveAsPlop)
    }

    def destroy = {}

    private void saveAsPlop(String text) {
        Plop plop = new Plop(text).save()
        log.info('{} saved.', plop)
    }
}
