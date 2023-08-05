package pub.gdt.project.daemon.event;

public abstract class SourcelessEvent extends Event<Object> {
    public SourcelessEvent() {
        super(new Object());
    }
}
