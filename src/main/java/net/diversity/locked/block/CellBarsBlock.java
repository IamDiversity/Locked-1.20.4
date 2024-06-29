package net.diversity.locked.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.List;

public class CellBarsBlock extends Block {

    public static final VoxelShape[] SHAPES = new VoxelShape[] {
            VoxelShapes.empty(),
            VoxelShapes.empty(),
            VoxelShapes.empty(),
            VoxelShapes.empty(),
            VoxelShapes.empty(),
            VoxelShapes.empty(),
            VoxelShapes.empty(),
            VoxelShapes.empty()
    };


    public enum BarsShape implements StringIdentifiable {
        north, south, east, west, north_east, north_west, south_east, south_west;

        @Override
        public String asString() {
            return name();
        }

        static {
            SHAPES[north.ordinal()] = Block.createCuboidShape(0,0,0,16,16,2);
            SHAPES[east.ordinal()] = Block.createCuboidShape(14,0,0,16,16,16);
            SHAPES[west.ordinal()] = Block.createCuboidShape(0,0,0,2,16,16);
            SHAPES[south.ordinal()] = Block.createCuboidShape(0,0,14,16,16,16);

            SHAPES[north_east.ordinal()] = VoxelShapes.union(SHAPES[north.ordinal()], SHAPES[east.ordinal()]);
            SHAPES[north_west.ordinal()] = VoxelShapes.union(SHAPES[north.ordinal()], SHAPES[west.ordinal()]);
            SHAPES[south_east.ordinal()] = VoxelShapes.union(SHAPES[south.ordinal()], SHAPES[east.ordinal()]);
            SHAPES[south_west.ordinal()] = VoxelShapes.union(SHAPES[south.ordinal()], SHAPES[west.ordinal()]);
        }

        public static BarsShape getShapeForDirections(Direction dir1, Direction dir2) {
            return getShapeForDirection(dir1).combineWith(dir2);
        }

        public boolean hasDirection(Direction dir, BlockState neighbourState) {
            //*return switch(dir) {
                /*case NORTH -> switch (this) {
                    case north, north_east, north_west -> true;
                    default -> false;
                };
                case EAST -> switch (this) {
                    case east, south_east, north_east -> true;
                    default -> false;
                };
                // south and west as well
                case SOUTH -> switch(this) {
                    case south, south_east, south_west -> true;
                    default -> false;
                };
                case WEST -> switch(this) {
                    case west, south_west, north_west -> true;
                    default -> false;
                };*/
                if (neighbourState.getBlock().equals(ModBlocks.CELL_BARS)) {
                    return switch(dir) {
                        case NORTH -> switch(this) {
                            case north, north_east, north_west -> true;
                            default -> false;
                        };
                        case EAST -> switch (this) {
                            case east, south_east, north_east -> true;
                            default -> false;
                        };
                        // south and west as well
                        case SOUTH -> switch(this) {
                            case south, south_east, south_west -> true;
                            default -> false;
                        };
                        case WEST -> switch(this) {
                            case west, south_west, north_west -> true;
                            default -> false;
                        };
                        default -> throw new IllegalArgumentException("Cannot get BarsShape for Direction " + dir);
                    };
                } else {
                    return false;
                }
            };
        }

        public static BarsShape getShapeForDirection(Direction dir) {
            return switch (dir) {
                case NORTH -> BarsShape.north;
                case SOUTH -> BarsShape.south;
                case WEST -> BarsShape.west;
                case EAST -> BarsShape.east;
                default -> throw new IllegalArgumentException("Cannot get BarsShape for Direction " + dir);
            };
        }

        public BarsShape combineWith(Direction other) {
            return switch(this) {
                case north -> switch(other) {
                    case EAST -> north_east;
                    case WEST -> north_west;
                    default -> null;
                };
                case east -> switch(other) {
                    case NORTH -> north_east;
                    case SOUTH -> south_east;
                    default -> null;
                };
                // continue this for south and west
                case south -> switch(other) {
                    case EAST -> south_east;
                    case WEST -> south_west;
                    default -> null;
                };

                case west -> switch (other) {
                    case NORTH -> north_west;
                    case SOUTH -> south_west;
                    default -> null;
                };

                default -> null;
            };
        }

        public Direction getDirection() {
            return switch(this) {
                case north -> Direction.NORTH;
                case south -> Direction.SOUTH;
                case east -> Direction.EAST;
                case west -> Direction.WEST;

                default -> null;
            };
        }
    }

    public static final EnumProperty<BarsShape> SHAPE = EnumProperty.of("shape", BarsShape.class);


    public CellBarsBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES[state.get(SHAPE).ordinal()];
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        world.updateNeighbors(pos, this);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighbourState, WorldAccess world, BlockPos pos, BlockPos neighbourPos) {

        if (state.contains(SHAPE) && direction.getAxis() != Direction.Axis.Y) {
            BarsShape base = BarsShape.getShapeForDirection(direction);
            BarsShape barsShape = state.get(SHAPE);

            Direction left = direction.rotateCounterclockwise(Direction.Axis.Y);
            if (barsShape.hasDirection(left, neighbourState)) {
                return this.getDefaultState().with(SHAPE, base.combineWith(left));
            }

            Direction right = direction.rotateClockwise(Direction.Axis.Y);
            if (barsShape.hasDirection(right, neighbourState)) {
                return this.getDefaultState().with(SHAPE, base.combineWith(right));
            }
            return this.getDefaultState().with(SHAPE, base);
        }

        return state;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(SHAPE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        Direction facingDir = context.getHorizontalPlayerFacing().getOpposite();
        BarsShape base = BarsShape.getShapeForDirection(facingDir);

        BlockState oppositeBlockState = context.getWorld().getBlockState(context.getBlockPos().offset(facingDir.getOpposite()));

        if (oppositeBlockState.contains(SHAPE)) {
            BarsShape oppositeBarsShape = oppositeBlockState.get(SHAPE);

            Direction left = facingDir.rotateCounterclockwise(Direction.Axis.Y);
            if (oppositeBarsShape.hasDirection(left)) {
                return this.getDefaultState().with(SHAPE, base.combineWith(left));
            }

            // then we need to repeat the left stuff for right
            Direction right = facingDir.rotateClockwise(Direction.Axis.Y);
            if (oppositeBarsShape.hasDirection(right)) {
                return this.getDefaultState().with(SHAPE, base.combineWith(right));
            }

        }

        // if we cant combine with left or right then just use the base shape
        return this.getDefaultState().with(SHAPE, base);
    }



}
