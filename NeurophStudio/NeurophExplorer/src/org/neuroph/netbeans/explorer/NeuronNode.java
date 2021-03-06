package org.neuroph.netbeans.explorer;

import java.awt.Image;
import java.util.concurrent.Callable;
import org.neuroph.core.Neuron;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.netbeans.properties.InputFunctionEditor;
import org.neuroph.netbeans.properties.NetInputEditor;
import org.neuroph.netbeans.properties.NetOutputEditor;
import org.neuroph.netbeans.properties.NeuronErrorEditor;
import org.neuroph.netbeans.properties.NeuronLabelEditor;
import org.neuroph.netbeans.properties.TransferFunctionEditor;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Ivana
 *
 * http://bits.netbeans.org/dev/javadoc/org-openide-nodes/org/openide/nodes/Node.html#Node%28org.openide.nodes.Children,%20org.openide.util.Lookup%29
 */
public class NeuronNode extends AbstractNode {

    private final Neuron neuron;

    public NeuronNode(Neuron neuron) {
        super(Children.createLazy(new ChildrenSetCallable(neuron)), Lookups.singleton(neuron));
        this.neuron = neuron;
    }

    public Neuron getNeuron() {
        return neuron;
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/neuroph/netbeans/explorer/icons/neuronIcon.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("Neuron properties");

        try {
            Property type = new PropertySupport.Reflection(neuron, Class.class, "getClass", null);
            PropertySupport.Reflection transferfunction = new PropertySupport.Reflection(neuron, TransferFunction.class, "getTransferFunction", "setTransferFunction");
            PropertySupport.Reflection inputfunction = new PropertySupport.Reflection(neuron, InputFunction.class, "getInputFunction", "setInputFunction");

            PropertySupport.Reflection netInput = new PropertySupport.Reflection(neuron, Double.TYPE, "getNetInput", "setInput");
            PropertySupport.Reflection output = new PropertySupport.Reflection(neuron, Double.TYPE, "getOutput", "setOutput");
          
            PropertySupport.Reflection error = new PropertySupport.Reflection(neuron, Double.TYPE, "getError", "setError");
            PropertySupport.Reflection label = new PropertySupport.Reflection(neuron, String.class, "getLabel", "setLabel");

            type.setShortDescription("Neuron type/class");
            inputfunction.setShortDescription("Input function");
            transferfunction.setShortDescription("Transfer function");
            netInput.setShortDescription("Total net input for this neuron");
            output.setShortDescription("Neuron's output");
            error.setShortDescription("Neuron's error");
            label.setShortDescription("Neuron's label");

            inputfunction.setPropertyEditorClass(InputFunctionEditor.class);
            transferfunction.setPropertyEditorClass(TransferFunctionEditor.class);
            label.setPropertyEditorClass(NeuronLabelEditor.class);
            netInput.setPropertyEditorClass(NetInputEditor.class);
            output.setPropertyEditorClass(NetOutputEditor.class);
            error.setPropertyEditorClass(NeuronErrorEditor.class);

            type.setName("Type");
            inputfunction.setName("Input function");
            transferfunction.setName("Transfer function");
            netInput.setName("Net Input");
            output.setName("Output");
            error.setName("Error");
            label.setName("Label");

            set.put(type);
            set.put(inputfunction);
            set.put(transferfunction);
            set.put(netInput);
            set.put(output);
            set.put(error);
            set.put(label);

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        sheet.put(set);
        return sheet;
    }
    
    // To enable creating leaf nodes at start, otherwise we need to pass a factory,
    // which can expand only manually and then check that there are no children nodes
    private static class ChildrenSetCallable implements Callable<Children> {

        private final Neuron key;

        private ChildrenSetCallable(Neuron key) {
            this.key = key;
        }

        @Override
        public Children call() throws Exception {
            if (key.getInputConnections().size() == 0) {
                return Children.LEAF;
            } else {
                // synchronous so that selection of members doesn't miss (if everything was not yet generated)
                return Children.create(new NeuronChildFactory(key), false);
            }
        }
    }
}
