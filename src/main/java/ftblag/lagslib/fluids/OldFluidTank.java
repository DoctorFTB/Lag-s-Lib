package ftblag.lagslib.fluids;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class OldFluidTank extends FluidTank {

    protected boolean canFill = true;
    protected boolean canDrain = true;

    public OldFluidTank(int capacity) {
        super(capacity);
    }

    public OldFluidTank(int capacity, Predicate<FluidStack> validator) {
        super(capacity, validator);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (!canFillFluidType(resource)) {
            return 0;
        }
        return fillInternal(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (!canDrainFluidType(fluid)) {
            return FluidStack.EMPTY;
        }
        return drainInternal(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (!canDrainFluidType(fluid)) {
            return FluidStack.EMPTY;
        }
        return drainInternal(maxDrain, action);
    }

    public int fillInternal(FluidStack resource, FluidAction action) {
        return super.fill(resource, action);
    }

    @Nonnull
    public FluidStack drainInternal(FluidStack resource, FluidAction action) {
        return super.drain(resource, action);
    }

    @Nonnull
    public FluidStack drainInternal(int maxDrain, FluidAction action) {
        return super.drain(maxDrain, action);
    }

    public boolean canFill()
    {
        return canFill;
    }

    public boolean canDrain()
    {
        return canDrain;
    }

    public void setCanFill(boolean canFill)
    {
        this.canFill = canFill;
    }

    public void setCanDrain(boolean canDrain)
    {
        this.canDrain = canDrain;
    }

    public boolean canFillFluidType(FluidStack fluid)
    {
        return canFill();
    }

    public boolean canDrainFluidType(@Nonnull FluidStack fluid)
    {
        return canDrain();
    }
}
